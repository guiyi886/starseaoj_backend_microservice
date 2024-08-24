package com.starseaoj.judgeservice.judge.codesandbox.impl;


import com.starseaoj.judgeservice.judge.codesandbox.CodeSandbox;
import com.starseaoj.model.codesandbox.ExecuteCodeRequest;
import com.starseaoj.model.codesandbox.ExecuteCodeResponse;

/**
 * @author guiyi
 * @Date 2024/8/11 下午4:04:24
 * @ClassName com.guiyi.starseaoj.judge.codesandbox.impl.CodeSandboxImpl
 * @function --> 第三方代码沙箱（使用网上现成的）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
