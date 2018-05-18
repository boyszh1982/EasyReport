package com.easytoolsoft.easyreport.web.controller.home;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.easytoolsoft.easyreport.common.tree.EasyUITreeNode;
import com.easytoolsoft.easyreport.membership.domain.Module;
import com.easytoolsoft.easyreport.membership.domain.User;
import com.easytoolsoft.easyreport.membership.service.impl.MembershipFacadeServiceImpl;
import com.easytoolsoft.easyreport.support.annotation.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Home页控制器
 *
 * @author Tom Deng
 * @date 2017-03-25
 */
@Controller
@RequestMapping(value = {"", "/", "/home"})
public class HomeController {
    @Resource
    private MembershipFacadeServiceImpl membershipFacade;

    @GetMapping(value = {"", "/", "/index"})
    public String index(@CurrentUser final User loginUser, final Model model) {
        model.addAttribute("roleNames", this.membershipFacade.getRoleNames(loginUser.getRoles()));
        model.addAttribute("user", loginUser);

        //code by stanley for button hide at 20180517
        final Set<String> permissionSet = this.membershipFacade.getPermissionSet(loginUser.getRoles());
        model.addAttribute("permissionSet", permissionSet);

        return "home/index";
    }

    @ResponseBody
    @GetMapping(value = "/rest/home/getMenus")
    public List<EasyUITreeNode<Module>> getMenus(@CurrentUser final User loginUser) {
        final List<Module> modules = this.membershipFacade.getModules(loginUser.getRoles());
        return this.membershipFacade.getModuleTree(modules, x -> x.getStatus() == 1);
    }

    //code by stanley for button hide at 20180518
    @PostMapping(value = "/rest/home/getPermissionSet")
    public @ResponseBody Object getPermissionSet(@CurrentUser final User loginUser){
        JSONObject jsonObject = new JSONObject();
        final Set<String> permissionSet = this.membershipFacade.getPermissionSet(loginUser.getRoles());
        jsonObject.put("permissionSet",permissionSet);
        //jsonObject.put("loginUser",loginUser);
        jsonObject.put("account",loginUser.getAccount());
        return jsonObject;
    }
}