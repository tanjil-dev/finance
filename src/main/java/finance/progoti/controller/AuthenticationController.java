package finance.progoti.controller;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import finance.progoti.service.UserService;
import finance.progoti.user.model.Bill;
import finance.progoti.user.model.Role;
import finance.progoti.user.model.User;

@Controller
public class AuthenticationController {
	
	@Autowired
	UserService userService;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login(@RequestParam boolean error, ModelAndView modelAndView)
	{
		if(error) {
			
			modelAndView.addObject("error", true);
			modelAndView.setViewName("login");
			return modelAndView;
		}
		return null;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView modelAndView = new ModelAndView();
		 User user = new User();
		 modelAndView.addObject("user", user); 
		modelAndView.setViewName("register"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/register", method=RequestMethod.POST)
	public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		//validation
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the error is form!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			modelAndView.addObject("successMessage", "User already exists");
		}
		else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User is registered successfully");
		}
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("redirect:/register");
		return modelAndView;
	}
	
	@RequestMapping(value = "/head_register", method = RequestMethod.GET)
	public ModelAndView registerHead() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user); 
		modelAndView.setViewName("registerHead"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/head_register", method=RequestMethod.POST)
	public ModelAndView registerHeadUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap, Role role) {
		ModelAndView modelAndView = new ModelAndView();
		//validation
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the error is form!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			modelAndView.addObject("successMessage", "User already exists");
		}
		else {
			role.setId(2);
			userService.saveHeadUser(user,role);
			modelAndView.addObject("successMessage", "User is registered successfully");
		}
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("redirect:/head_register");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin_register", method = RequestMethod.GET)
	public ModelAndView registerAdmin() {
		ModelAndView modelAndView = new ModelAndView();
		 User user = new User();
		 modelAndView.addObject("user", user); 
		 modelAndView.setViewName("registerAdmin"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin_register", method=RequestMethod.POST)
	public ModelAndView registerAdminUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap, Role role) {
		ModelAndView modelAndView = new ModelAndView();
		//validation
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the error is form!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			modelAndView.addObject("successMessage", "User already exists");
		}
		else {
			//role.setId(1);
			userService.saveAdminUser(user);
			modelAndView.addObject("successMessage", "User is registered successfully");
		}
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("redirect:/admin_register");
		return modelAndView;
	}
	
}
