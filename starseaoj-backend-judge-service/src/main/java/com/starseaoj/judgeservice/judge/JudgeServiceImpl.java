package com.starseaoj.judgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.starseaoj.common.common.ErrorCode;
import com.starseaoj.common.exception.BusinessException;
import com.starseaoj.judgeservice.judge.codesandbox.CodeSandbox;
import com.starseaoj.judgeservice.judge.codesandbox.CodeSandboxFactory;
import com.starseaoj.judgeservice.judge.codesandbox.CodeSandboxProxy;
import com.starseaoj.judgeservice.judge.strategy.JudgeContext;
import com.starseaoj.model.codesandbox.ExecuteCodeRequest;
import com.starseaoj.model.codesandbox.ExecuteCodeResponse;
import com.starseaoj.model.codesandbox.JudgeInfo;
import com.starseaoj.model.dto.question.JudgeCase;
import com.starseaoj.model.entity.Question;
import com.starseaoj.model.entity.QuestionSubmit;
import com.starseaoj.model.enums.QuestionSubmitStatusEnum;
import com.starseaoj.serviceclient.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guiyi
 * @Date 2024/8/11 下午6:21:32
 * @ClassName com.guiyi.starseaoj.judge.JudgeServiceImpl
 * @function -->
 */
@Service // 和 @Component 没有实际区别，仅做语义区分，表示这是服务层
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    /**
     * 从配置文件中获取type，设置默认值为thirdParty
     */
    @Value("${codesandbox.type:thirdParty}")
    private String type;

    @Override
    @Async  // 异步执行
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1.传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 2.如果题目提交状态不为 “待判题”，就不往下执行
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        // 3.更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }

        // 4.调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        // 使用codeSandbox创建代理类对象重新赋值给codeSandbox
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();

        // 获取输入样例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream()
                .map(JudgeCase::getInput)
                .collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();

        // 5.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
