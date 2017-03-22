package com.crazy.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crazy.bean.TUser;
import com.crazy.service.TUserService;
import com.crazy.bean.PageBean;

@Controller
@RequestMapping("/user")
public class TUserController {
	
	@Resource
	private TUserService userService;
	
	/**
	 * ����û�
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/addUser")
	public String addUser(TUser user,HttpServletRequest request){
		if(user.getUname() == null){
			user.setUname("crazy");
			user.setUpwd("123");
			user.setUmessage("����");
		}
		user.setUtime(new Date());
		int resultCount=userService.insert(user);
		if(resultCount > 0){
			request.setAttribute("addmess", "�û���ӳɹ���");
		}else{
			request.setAttribute("addmess", "�û����ʧ�ܣ�");
		}
		return "index";
	}
	
	/**
	 * ��ѯ�����û�
	 * @param user
	 * @param page
	 * @param rows
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/allUser")
	public String selectUser(TUser user,@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,HttpServletRequest request){
		PageBean pageBean=null;
		if(page == null && rows == null){
			pageBean=new PageBean(1,10);
		}else{
			pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		}
		Map<String, Object> map=new HashMap<>();
		map.put("uname", user.getUname());
		map.put("umessage", user.getUmessage());
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		List<TUser> list=userService.queryAll(map);
		Long total=userService.getTotal(map);
		request.setAttribute("list", list);
		request.setAttribute("total", total);
		return "index";
	}
	
	/**
	 * �޸��û���Ϣ
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upUser")
	public String updateUser(TUser user,HttpServletRequest request){
		if(user.getUid() != null){
			int resultcount=userService.updateByPrimaryKey(user);
			if(resultcount > 0){
				request.setAttribute("upmess", "�޸ĳɹ���");
			}else{
				request.setAttribute("upmess", "�޸�ʧ�ܣ�");
			}
		}
		return "index";
	}
	
	/**
	 * ɾ���û�
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delUser")
	public String deleteUser(Integer uid,HttpServletRequest request){
		if(uid != null){
			int resultcount=userService.deleteByPrimaryKey(uid);
			if(resultcount > 0){
				request.setAttribute("delmess", "ɾ���ɹ���");
			}else{
				request.setAttribute("delmess", "ɾ��ʧ�ܣ�");
			}
		}
		return "index";
	}
}
