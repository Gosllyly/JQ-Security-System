package com.jqmk.examsystem.framwork.aop;

import com.jqmk.examsystem.errors.ErrorCodeEnum;
import com.jqmk.examsystem.exception.BizException;
import com.jqmk.examsystem.utils.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName JwtInterceptor
 * @Author tian
 * @Date 2024/6/5 13:46
 * @Description token验证
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("token");

        // token不存在时，返回信息
        if (token == null) {
            throw new BizException(ErrorCodeEnum.TOKEN_NONE);
        }
        // 对token进行验证,验证
        if (!JwtUtil.checkToken(token)) {
            throw new BizException(ErrorCodeEnum.TOKEN_FAIL_VERIFY);
        }
        // 判断token是否过期
        if (JwtUtil.isExpiration(token)) {
            throw new BizException(ErrorCodeEnum.TOKEN_OVERDUE);
        }
        return true;
    }
}