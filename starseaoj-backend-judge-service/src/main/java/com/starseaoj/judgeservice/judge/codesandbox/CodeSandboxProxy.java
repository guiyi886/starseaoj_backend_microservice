package com.starseaoj.judgeservice.judge.codesandbox;

import com.starseaoj.model.codesandbox.ExecuteCodeRequest;
import com.starseaoj.model.codesandbox.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guiyi
 * @Date 2024/8/11 下午5:30:46
 * @ClassName com.guiyi.starseaoj.judge.codesandbox.CodeSandboxProxy
 * @function --> 代码沙箱代理类
 */
@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求参数：{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应结果：{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
