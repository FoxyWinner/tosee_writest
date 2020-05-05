package com.tosee.tosee_writest;

import com.tosee.tosee_writest.exception.WritestException;
import com.tosee.tosee_writest.utils.ResultVOUtil;
import com.tosee.tosee_writest.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: FoxyWinner
 * @Date: 2020/4/30 4:28 下午
 */
@ControllerAdvice
public class WritestExceptionHandler
{
//    @Autowired
//    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常，未登陆的话跳转到扫码登录界面
    //https://fubaionline.mynatapp.cc/onlinestore/wechat/qrAuthorize?returnUrl=http://fubaionline.mynatapp.cc/onlinestore/seller/login
//    @ExceptionHandler(value = SellerAuthorizeException.class)
//    public ModelAndView handlerAuthorizeException()
//    {
//        return new ModelAndView("redirect:"
//                .concat(projectUrlConfig.getWechatOpenAuthorize())
//                .concat("/onlinestore/wechat/qrAuthorize")
//                .concat("?returnUrl=")
//                .concat(projectUrlConfig.getOnlinestore()
//                        .concat("/onlinestore/seller/login")));
//    }

    @ExceptionHandler(value = WritestException.class)
    @ResponseBody
    public ResultVO handlerSellException(WritestException e)
    {
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }
}
