package com.starseaoj.judgeservice.judge;


import com.starseaoj.model.entity.QuestionSubmit;

/**
 * @author guiyi
 * @Date 2024/8/11 下午6:01:51
 * @ClassName com.guiyi.starseaoj.judge.JudgeService
 * @function --> 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
