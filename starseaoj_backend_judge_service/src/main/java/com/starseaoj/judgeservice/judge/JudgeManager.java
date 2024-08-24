package com.starseaoj.judgeservice.judge;

import com.starseaoj.judgeservice.judge.strategy.DefaultJudgeStrategy;
import com.starseaoj.judgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.starseaoj.judgeservice.judge.strategy.JudgeContext;
import com.starseaoj.judgeservice.judge.strategy.JudgeStrategy;
import com.starseaoj.model.codesandbox.JudgeInfo;
import com.starseaoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @author guiyi
 * @Date 2024/8/12 上午12:28:28
 * @ClassName com.guiyi.starseaoj.judge.JudgeManager
 * @function --> 判题管理（简化调用）
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
