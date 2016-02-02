package cn.howardliu.demo.websocket.xml.echo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br/>create at 16-2-2
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
public class IndexView {
    @RequestMapping("index")
    public String index() {
        return "index";
    }
}
