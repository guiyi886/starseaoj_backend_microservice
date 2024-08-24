package com.starseaoj.judgeservice.judge.codesandbox.impl;


import com.starseaoj.judgeservice.judge.codesandbox.CodeSandbox;
import com.starseaoj.model.codesandbox.ExecuteCodeRequest;
import com.starseaoj.model.codesandbox.ExecuteCodeResponse;
import com.starseaoj.model.codesandbox.JudgeInfo;
import com.starseaoj.model.enums.JudgeInfoMessageEnum;
import com.starseaoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @author guiyi
 * @Date 2024/8/11 下午4:04:24
 * @ClassName com.guiyi.starseaoj.judge.codesandbox.impl.CodeSandboxImpl
 * @function --> 示例代码沙箱（仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        // 使用executeCodeResponse.allset快速生成set方法
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }
}
