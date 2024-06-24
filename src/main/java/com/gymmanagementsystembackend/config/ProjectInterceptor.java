package com.gymmanagementsystembackend.config;

import com.auth0.jwt.interfaces.Claim;
import com.gymmanagementsystembackend.model.RedisDataModel;
import com.gymmanagementsystembackend.tool.Code;
import com.gymmanagementsystembackend.tool.JwtToken;
import com.gymmanagementsystembackend.tool.ServeTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class ProjectInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    @Autowired
    private ServeTool serveTool;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String clickDate=simpleDateFormat.format(date).replace("-","_");;

        Integer usedClickData=serveTool.<Integer>getManagerRedisData(Code.interFaceClicks_data_prefix+"_"+clickDate,Integer.class);
        Integer newClickData=1;
        if (usedClickData!=null)newClickData=usedClickData+1;

        RedisDataModel<Integer> redisDataModel=new RedisDataModel<>();
        redisDataModel.setKey(Code.interFaceClicks_data_prefix+"_"+clickDate);
        redisDataModel.setData(newClickData);
        redisDataModel.setTimeout(60*60*24*7);
        serveTool.addManagerRedisData(redisDataModel);

        String token=request.getHeader("Authorization");
        if (token!=null&&!"".equals(token)){
            Map<String,Claim> claimMap=JwtToken.verifyToken(token);
            String identityId=claimMap.get("identityId").toString();

            THREAD_LOCAL.set(identityId);

            return true;
        }

        response.setStatus(Code.not_token);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public static String getDataFromThreadLocal() {
        return THREAD_LOCAL.get();
    }
}
