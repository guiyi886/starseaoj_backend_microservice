package com.starseaoj.judgeservice.judge.codesandbox;


import com.starseaoj.model.codesandbox.ExecuteCodeRequest;
import com.starseaoj.model.codesandbox.ExecuteCodeResponse;

/**
 * @author guiyi
 * @Date 2024/8/11 下午3:37:10
 * @ClassName com.guiyi.starseaoj.judge.codesandbox.model.CodeSandbox
 * @function -->    代码沙箱接口
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
