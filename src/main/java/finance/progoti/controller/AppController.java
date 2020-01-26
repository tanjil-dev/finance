package finance.progoti.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import finance.progoti.service.NotificationService;
import finance.progoti.service.ProductService;

import finance.progoti.service.UserServiceImp;
import finance.progoti.storage.StorageFileNotFoundException;
import finance.progoti.storage.StorageService;
import finance.progoti.user.model.Product;

import finance.progoti.user.model.User;

@Controller
public class AppController {
	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	private ProductService service;
	@Autowired
	private UserServiceImp userServiceImp;
	@Autowired
    private StorageService storageService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ServletContext context;
	
	/*
	 * @RequestMapping(value = "/createPdf", method = RequestMethod.GET ) public
	 * void createPdf(HttpServletRequest request, HttpServletResponse response) {
	 * List<Product> listBills = service.listAllProduct(); boolean isFlag =
	 * service.createPdf(listBills, context, request, response); if(isFlag) { String
	 * fullPath =
	 * request.getServletContext().getRealPath("/resources/reports/"+"listBills"+
	 * ".pdf"); filedownload(fullPath,response,"listBills.pdf"); } }
	 */
	
	@RequestMapping(value = "/createExcel", method = RequestMethod.GET )
	public void createExcel(HttpServletRequest request,HttpServletResponse response) {
		List<Product> listBills = service.listAll();
		boolean isFlag = service.createExcel(listBills, context, request, response);
		if(isFlag)
		{
			String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"listBills"+".xls");
			filedownload(fullPath,response,"listBills.xls");
		}
	}
	
	@RequestMapping(value = "/createHeadExcel", method = RequestMethod.GET )
	public void createHeadExcel(HttpServletRequest request,HttpServletResponse response) {
		List<Product> listBills = service.listAll();
		boolean isFlag = service.createExcel(listBills, context, request, response);
		if(isFlag)
		{
			String fullPath = request.getServletContext().getRealPath("/resources/reports/"+"listBills"+".xls");
			filedownload(fullPath,response,"listBills.xls");
		}
	}
 
	private void filedownload(String fullPath, HttpServletResponse response, String fileName) {
		
		File file = new File(fullPath);
		final int BUFFER_SIZE = 4096;
		if(file.exists())
		{
			try {
				
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("content-disposition", "attachment; filename"+fileName);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while((bytesRead = inputStream.read(buffer)) != -1)
				{
					outputStream.write(buffer, 0, bytesRead);
					
				}
				
				inputStream.close();
				outputStream.close();
				file.delete();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
	public String viewHomePage(Model model, @SessionAttribute("user") User user) {
			List<Product> listBills = service.findBySubmittedby(user.getEmail());
		 	listBills.contains(model.addAttribute("listBills", service.findByApprovedby(null)));	
			model.addAttribute("user", user);
			return "home";				

	}
	
	@RequestMapping(value = "/super_admin", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
	public String viewSuperAdminPage(Model model, @SessionAttribute("user") User user) {
			List<User> users = userServiceImp.listAllUser();
			model.addAttribute("users", users);
			model.addAttribute("user", user);
			return "super_admin";				
	}
	
	/*
	 * @RequestMapping(value = "/reset_password/{id}", method = RequestMethod.GET)
	 * public ModelAndView resetPassword( @PathVariable(name = "id") int id) {
	 * ModelAndView mav = new ModelAndView("/resetPassword");
	 * 
	 * User user = userServiceImp.get(id);
	 * user.setPassword(encoder.encode(user.getPassword())); mav.addObject("user",
	 * user);
	 * 
	 * return mav; }
	 */
	
	@RequestMapping(value = "/add_note/{id}", method = RequestMethod.GET)
	public ModelAndView addNote(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("/add_note");
		
		Product bill = service.get(id);
		mav.addObject("bill", bill);
		
		return mav;
	}
	
	/*
	 * @RequestMapping(value = "/save_password", method = RequestMethod.POST) public
	 * String savePassword( RedirectAttributes redirectAttributes,
	 * HttpServletRequest request, int id) throws Exception{ HttpSession session =
	 * request.getSession(false);
	 * 
	 * User user = (User)session.getAttribute("user");
	 * 
	 * final User savePassword = userServiceImp.get(id); if (savePassword == null) {
	 * return null; }
	 * 
	 * savePassword.setPassword(encoder.encode(user.getPassword()));;
	 * userServiceImp.saveUser(savePassword);
	 * //notificationService.sendNotification(user,savePassword); return
	 * "redirect:/super_admin"; }
	 */
	
	@RequestMapping(value = "/save_note", method = RequestMethod.POST)
	public String saveNote(
        RedirectAttributes redirectAttributes, @ModelAttribute("product") Product product, HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		final Product savedProduct = service.get(product.getId());
		if (savedProduct == null) {
			return null;
		}
		
		savedProduct.setNote(product.getNote());
		service.save(savedProduct);
		notificationService.sendNotification(user,savedProduct);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/save_headNote", method = RequestMethod.POST)
	public String saveHeadNote(
        RedirectAttributes redirectAttributes, @ModelAttribute("product") Product product, HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		final Product savedProduct = service.get(product.getId());
		if (savedProduct == null) {
			return null;
		}
		
		savedProduct.setNote(product.getNote());
		service.save(savedProduct);
		notificationService.sendNotification(user,savedProduct);
		return "redirect:/admin_home";
	}

	@RequestMapping(value = "/adminbillhome", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
	public String viewHomeBillPage(Model model, @SessionAttribute("user") User user) {
			List<Product> listBills = service.findBySubmittedby(user.getEmail());
	 		listBills.contains(model.addAttribute("listBills", service.findByApprovedby(null)));
			model.addAttribute("user", user);
			return "adminBill";				

	}
	
	@RequestMapping(value = "/admin_homeBill", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
	public String viewAdminBillHomePage(Model model, @SessionAttribute("user") User user) {
			List<Product> listBills = service.findBySubmittedby(user.getEmail());
	 		listBills.contains(model.addAttribute("listBills", service.findByApprovedby(null)));
			model.addAttribute("user", user);
			return "admin_homeBill";				

	}
	
	@RequestMapping(value = "/history", method = RequestMethod.GET )
	public String viewHistoryPage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.findBySubmittedby(user.getEmail());
		model.addAttribute("listBills",listBills);
		model.addAttribute("user", user);
		return "history";
	}
	
	@RequestMapping(value = "/adminBill_history", method = RequestMethod.GET )
	public String viewAdminHistoryPage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.findBySubmittedby(user.getEmail());
		model.addAttribute("listBills",listBills);
		model.addAttribute("user", user);
		return "adminBillHistory";
	}
	
	@RequestMapping(value = "/admin_homeBill_history", method = RequestMethod.GET )
	public String viewAdminHomeHistoryPage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.findBySubmittedby(user.getEmail());
		model.addAttribute("listBills",listBills);
		model.addAttribute("user", user);
		return "adminHomeBillHistory";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET )
	public String viewAdminPage(Model model, @SessionAttribute("user") User user, Product product) {
		List<Product> listBills = service.listAll();
 		listBills.contains(model.addAttribute("listBills", service.findByPaidby(null)));
		model.addAttribute("user", user);
		return "admin";
	}
	
	@RequestMapping(value = "/historyAdminPay", method = RequestMethod.GET )
	public String viewHistoryAdminPayPage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.findByPaidby(user.getEmail());
		model.addAttribute("listBills",listBills);
		model.addAttribute("user", user);
		return "historyAdminPay";
	}
	
	@RequestMapping(value = "/admin_home", method = RequestMethod.GET)
	public String viewAdminHomePage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.listAll();
		
		listBills.contains(model.addAttribute("listBills", service.findByApprovedby(null)));
		model.addAttribute("user", user);
		return "admin_home";
	}
	
	@RequestMapping(value = "/historyAdmin", method = RequestMethod.GET )
	public String viewHistoryAdminPage(Model model, @SessionAttribute("user") User user) {
		List<Product> listBills = service.findByApprovedby(user.getEmail());
		model.addAttribute("listBills",listBills);
		model.addAttribute("user", user);
		return "historyAdmin";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String showNewBillForm(Model model) {
		Product bill = new Product();
		model.addAttribute("bill", bill);
		
		return "new";
	}
	
	@RequestMapping(value = "/new_admin_bill", method = RequestMethod.GET)
	public String showAdminNewBillForm(Model model) {
		Product bill = new Product();
		model.addAttribute("bill", bill);
		
		return "newAdminBill";
	}
	
	@RequestMapping(value = "/new_admin_home_bill", method = RequestMethod.GET)
	public String showAdminHomeNewBillForm(Model model) {
		Product bill = new Product();
		model.addAttribute("bill", bill);
		
		return "newAdmin_homeBill";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveBill(@RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes, @ModelAttribute("product") Product product, HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		product.setSubmittedby(user.getEmail());
		
		String uniqueName = UUID.randomUUID().toString() + "_" + user.getName();
		storageService.store(file, uniqueName);
		product.setImagePath(uniqueName);
		redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + "!");
		service.save(product);
		notificationService.sendNotification(user,product);
		return "redirect:/home";
	}
	
	@RequestMapping(value = "/saveAdminBill", method = RequestMethod.POST)
	public String saveAdminBill(@RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes, @ModelAttribute("product") Product product, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		product.setSubmittedby(user.getEmail());
		
		String uniqueName = UUID.randomUUID().toString() + "_" + user.getName();
		storageService.store(file, uniqueName);
		product.setImagePath(uniqueName);
		redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + "!");
		service.save(product);
		
		return "redirect:/adminbillhome";
	}
	
	@RequestMapping(value = "/saveAdmin_homeBill", method = RequestMethod.POST)
	public String saveAdmin_homeBill(@RequestParam("file") MultipartFile file,
        RedirectAttributes redirectAttributes, @ModelAttribute("product") Product product, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		product.setSubmittedby(user.getEmail());
		
		String uniqueName = UUID.randomUUID().toString() + "_" + user.getName();
		storageService.store(file, uniqueName);
		product.setImagePath(uniqueName);
		redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + "!");
		service.save(product);
		
		return "redirect:/admin_homeBill";
	}

	 @RequestMapping(value = "/image/{name}",method=RequestMethod.GET)
     @ResponseBody
     public byte[] helloWorld(@PathVariable String name,HttpServletResponse response)  {
         System.out.println("Show is invoked");
         response.setContentType("image/jpeg");
         return storageService.loadResourceAsByte(name);
       } 

	 
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.POST)
	public String saveApproveBill(@PathVariable("id") long id, HttpServletRequest request) throws Exception{
		Product product = service.get(id);
		
		HttpSession session = request.getSession(false);
		if(session !=  null && product!= null) {
			User user = (User)session.getAttribute("user");
			product.setApprovedby(user.getEmail());
			service.save(product);
			notificationService.sendNotification(user,product);
		}
		
		return "redirect:/admin_home";
	}
	
	@RequestMapping(value = "/cancelapprove/{id}", method = RequestMethod.POST)
	public String cancelApproveBill(@PathVariable("id") long id, HttpServletRequest request) {
		Product product = service.get(id);
		
		HttpSession session = request.getSession(false);
		if(session !=  null && product!= null) {
			User user = (User)session.getAttribute("user");
			product.setApprovedby("");
			service.save(product);
		}
		
		return "redirect:/admin_home";
	}
	
	@RequestMapping(value = "/pay/{id}", method = RequestMethod.POST)
	public String savePayBill(@PathVariable("id") long id, HttpServletRequest request) throws MailException, Exception {
		Product product = service.get(id);
		
		HttpSession session = request.getSession(false);
		if(session !=  null && product != null) {
			
			User user = (User)session.getAttribute("user");
			product.setPaidby(user.getEmail());
			service.save(product);
			notificationService.sendNotification(user,product);
		}
		
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/cancelpay/{id}", method = RequestMethod.POST)
	public String cancelPayBill(@PathVariable("id") long id, HttpServletRequest request) {
		Product product = service.get(id);
		
		HttpSession session = request.getSession(false);
		if(session !=  null && product != null) {
			User user = (User)session.getAttribute("user");
			product.setPaidby("");
			service.save(product);
		}
		
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditBillForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("/edit_bill");
		
		Product bill = service.get(id);
		mav.addObject("bill", bill);
		
		return mav;
	}
	
	
	@RequestMapping(value = "/add_headNote/{id}", method = RequestMethod.GET)
	public ModelAndView addHeadNote(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("/add_headNote");
		
		Product bill = service.get(id);
		mav.addObject("bill", bill);
		
		return mav;
	}
	
	
	
	@RequestMapping(value = "/editAdminBill/{id}", method = RequestMethod.GET)
	public ModelAndView showEditAdminBillForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("/edit_adminBill");
		
		Product bill = service.get(id);
		mav.addObject("bill", bill);
		
		return mav;
	}
	
	@RequestMapping(value = "/editAdmin_homeBill/{id}", method = RequestMethod.GET)
	public ModelAndView showEditAdmin_homeBillForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("/editAdmin_homeBill");
		
		Product bill = service.get(id);
		mav.addObject("bill", bill);
		
		return mav;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteBill(@PathVariable(name = "id") Long id) {
		service.delete(id);
		
		return "redirect:/home";
	}	

	@ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}

