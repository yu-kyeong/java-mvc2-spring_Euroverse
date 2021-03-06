package com.ksy.web.room;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ksy.service.domain.Room;
import com.ksy.service.order.OrderService;
import com.ksy.service.room.RoomService;
import com.ksy.service.user.UserService;

@Controller
@RequestMapping("/room/*")
public class RoomController {

	@Autowired
	@Qualifier("orderServiceImpl")
	private OrderService orderService;

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("roomServiceImpl")
	private RoomService roomService;

	public RoomController() {
		System.out.println(this.getClass());
	}

	@Value("#{commonProperties['pageUnit']}")
	// @Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	// @Value("#{comm4onProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
//	@RequestMapping(value = "addFlight", method = RequestMethod.GET)
//	public String addFlight(@ModelAttribute("flight")Flight flight , Model model) throws Exception {
//		System.out.println("/addFlightOrder : GET");
//		
//		model.addAttribute("flight", flight);
//
//		return "forward:/flight/addFlight.jsp";
//	}
//
//	@RequestMapping(value = "addFlight", method = RequestMethod.POST)
//	public void addFlight(@ModelAttribute("flight")Flight flight,
//			HttpSession session, HttpServletRequest request) throws Exception {
//		System.out.println("/addFlightOrder : POST");
//		User user = (User) session.getAttribute("user");
//		System.out.println(user);
//
//		order.setBuyerId(user);
//		order.setOrderStatus("0");
//		
//		orderService.addFlightOrder(order);
//		Flight flight = new Flight();
//		flightService.addFlight(flight);
//		
//	}
	
//	@RequestMapping(value = "getRoom", method = RequestMethod.GET)
//	public String getRoom(@RequestParam("roomId")String roomId , Model model) throws Exception {
//		System.out.println("/getFlight : GET");
//		
//		Room room = roomService.getRoom(roomId);
//		model.addAttribute("room",room);
//		
//		return "forward:/room/getRoom.jsp";
//	}
	

	// Filed
	private WebDriver driver;
	private WebElement webElement;

	// Properties
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
	public static final String WEB_DRIVER_PATH = "C:/selenium-java-3.141.59/chromedriver_win32/chromedriver.exe";

	// ?????? ?? URL
	private String base_url;

	////////////////////////////////////////////////////////////
	@RequestMapping(value = "searchRoom", method = RequestMethod.POST)
	public String searchRoom(@ModelAttribute("room")Room room , Model model) throws Exception {
		System.out.println("/searchRoom : POST");
		
		//User?? ?????? ?? get
		String roomCity = room.getRoomCity();
		String checkIn = room.getCheckIn().replace("-", "");
		System.out.println("getCheckIn :: "+room.getCheckIn());
		System.out.println("checkIn :: "+checkIn );
		String checkOut = room.getCheckOut().replace("-", "");
		int roomNum = room.getRoomNum();
		int adultNum = room.getAdultNum();
		int adultNum2 = room.getAdultNum2();
		int childNum = room.getChildNum();
		int childNum2 = room.getChildNum2();
		//checkIn
		String year = checkIn.substring(0, 4); // ????
		String month = checkIn.substring(4, 6);// ??
		String date = checkIn.substring(6); // ??
		//checkOut
		String year2 = checkOut.substring(0, 4); // ????
		String month2= checkOut.substring(4, 6);// ??
		String date2 = checkOut.substring(6); // ??
		
		System.out.println("room : " + room);

		// crawl();
		// public synchronized void crawl() {

		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
		base_url = "https://kr.hotels.com/?intlid=HOME+%3A%3A+header_main_section";

		String keyword = "";
		try {
			// get page (= ???????????? url?? ???????? ???? ?? request ?? ???? ????)
			driver.get(base_url);

			System.out.println("???????????? ???? ????");
			// arrCity ????
			webElement = driver.findElement(By.xpath("//*[@id=\"qf-0q-destination\"]"));
			webElement.clear();
			synchronized (driver) {
				driver.wait(500);

			}
			// text ????
			webElement.sendKeys(roomCity);
			synchronized (driver) {
				driver.wait(500);

			}
			//???? ????
//			webElement = driver.findElement(By.cssSelector("#managed-overlay > button"));
//			webElement.click();
			
			// ???? ?? ????
			driver.findElement(By.xpath("//*[@id=\"hds-marquee\"]/div[2]/div[1]/div/p")).click();
			synchronized (driver) {
				driver.wait(500);

			}
			// ??,?? ?????? ???? :in
			webElement = driver.findElement(By.xpath("//*[@id=\"qf-0q-localised-check-in\"]"));
			webElement.clear();
			synchronized (driver) {
				driver.wait(500);
			}
			// ???? ???? :in
			webElement.sendKeys(year + "/" + month + "/" + date);
			synchronized (driver) {
				driver.wait(500);
			}
			// ???? ?? ???? :in
			driver.findElement(By.xpath("//*[@id=\"hds-marquee\"]/div[2]/div[1]/div/p")).click();
			synchronized (driver) {
				driver.wait(500);
			}
			// ??,?? ?????? ???? :out
			webElement = driver.findElement(By.xpath("//*[@id=\"qf-0q-localised-check-out\"]"));
			webElement.clear();
			synchronized (driver) {
				driver.wait(500);
			}
			// ???? ???? :out
			webElement.sendKeys(year2 + "/" + month2 + "/" + date2);
			synchronized (driver) {
				driver.wait(500);
			}
			// ???? ?? ???? :out
			driver.findElement(By.xpath("//*[@id=\"hds-marquee\"]/div[2]/div[1]/div/p")).click();
			synchronized (driver) {
				driver.wait(500);
			}
			// ???? ????
			driver.findElement(By.xpath("//*[@id=\"qf-0q-rooms\"]")).click();
			synchronized (driver) {
				driver.wait(300);
			}
			// ???? ???? ????
			driver.findElement(By.cssSelector("#qf-0q-rooms > option:nth-child(" + roomNum + ")")).click();
			synchronized (driver) {
				driver.wait(500);
			}
			//??????
			driver.findElement(By.cssSelector("#qf-0q-room-0-adults > option:nth-child("+adultNum+")")).click();
			synchronized (driver) {
				driver.wait(500);
			}
			//??????
			if (childNum > 0) {
				driver.findElement(By.cssSelector("#qf-0q-room-0-children > option:nth-child("+childNum+")")).click();
				synchronized (driver) {
					driver.wait(500);
				}
			}
			//??????2
			if (adultNum2 > 0) {
				driver.findElement(By.cssSelector("#qf-0q-room-1-adults > option:nth-child("+adultNum2+")")).click();
				synchronized (driver) {
					driver.wait(300);
				}
			}
			//??????2
			if (childNum2 > 0) {
				driver.findElement(By.cssSelector("#qf-0q-room-1-children > option:nth-child("+childNum2+")")).click();
				synchronized (driver) {
					driver.wait(300);
				}
			}
			// ???? ????
			driver.findElement(By.xpath("//*[@id=\"hds-marquee\"]/div[2]/div[1]/div/form/div[4]/button")).click();
			System.out.println("============");
			System.out.println("???????????? ???? ????");
			synchronized (driver) {
				driver.wait(800);
			}
			int pageDown = 4;
			webElement = driver.findElement(By.tagName("body"));
			while (pageDown > 0) {
				webElement = driver.findElement(By.tagName("body"));
				webElement.sendKeys(Keys.PAGE_DOWN);
				pageDown -= 1;
				synchronized (driver) {
					driver.wait(200);
				}
				System.out.println("?????? ????11");
			}
			synchronized (driver) {
				driver.wait(1000);
			}
			pageDown = 2;
			while (pageDown > 0) {
				webElement = driver.findElement(By.tagName("body"));
				webElement.sendKeys(Keys.PAGE_DOWN);
				pageDown -= 1;
				synchronized (driver) {
					driver.wait(200);
				}
				System.out.println("?????? ????22");
			}
			synchronized (driver) {
				driver.wait(1500);
			}
			pageDown = 3;
			while (pageDown > 0) {
				webElement = driver.findElement(By.tagName("body"));
				webElement.sendKeys(Keys.PAGE_DOWN);
				pageDown -= 1;
				synchronized (driver) {
					driver.wait(200);
				}
				System.out.println("?????? ????33");
			}
			synchronized (driver) {
				driver.wait(800);
			}
			//????????
			List<WebElement> name = driver
					.findElements(By.cssSelector("#listings > ol > li > article > section > div > h3 > a"));
			//???????? //xpath : ("//*[@id=\"listings\"]/ol/li/article/section/div/address/span")
			List<WebElement> addr = driver           
					.findElements(By.className("address"));
			//????????								
			List<WebElement> location = driver     
					.findElements(By.cssSelector("#listings > ol > li > article > section > div > div > div.additional-details.resp-module > div.location-info.resp-module > a"));
			// ???????????? ????
			List<WebElement> distance = driver   
					.findElements(By.cssSelector("#listings > ol > li > article > section > div > div > div > div > ul > li:nth-child(1)"));
			// ????									
			List<WebElement> grade = driver
					.findElements(By.cssSelector("#listings > ol > li > article > section > div > div > div.reviews-box.resp-module > strong"));
			//????
			List<WebElement> price = driver.findElements(
					By.cssSelector("#listings > ol > li > article > section > aside > div.price > a > ins"));
			/////////////////////////////////////??????////////////////////////////////////////////////////
			  
			
			
			  List<WebElement> roomImg = driver.findElements(By.cssSelector(
					  "#listings > ol > li > article > section > div > figure > a > img.u-photo.use-bgimage.featured-img-tablet"));
			  List<String> styleList = new ArrayList<String>();
			  for(WebElement down : roomImg) {
				   styleList.add(down.getAttribute("style"));
			  }
			  //String down = download.getAttribute("style"); //imgUrl 
			  System.out.println("StyleList : "+styleList);
			  List<String> imgSrc = new ArrayList<String>();
			  for(String tempt : styleList) {
				  int start = tempt.indexOf("(")+2; 
				  int end = tempt.lastIndexOf(")")-1;
				  System.out.println("Start : "+start+"End : "+end); 
				  String url =tempt.substring(start , end); 
				  System.out.println("URL : "+url);   
				  //list2.add(url);
				  
				  //???? ?? ????????!!!
				  String savePath =
						  "C:\\Users\\User\\git\\Euroverse\\ksy\\WebContent\\resources\\images\\roomImg\\";
						  //"C:\\Users\\User\\git\\repository\\Exhibition\\11.Model2MVCShop\\WebContent\\images\\uploadFiles\\";
				  String saveFileName = System.currentTimeMillis()+".jpg";
				  String fileFormat = "jpg";
				  
				  System.out.println(" DOWN ::: " + url); 
				  System.out.println(" SAVE PATH ::: "+ savePath); 
				  System.out.println(" SAVE FILE NAME ::: " + saveFileName);
				  System.out.println(" FILE FORMAT ::: " + fileFormat);
				  File saveFile = new File(savePath + saveFileName);
				  imgSrc.add(saveFileName);
				  //saveImage(down, saveFile, fileFormat);
				  
				  URL url2 = new URL(url); 
				  // ???????? ?? ?????? URL
				  BufferedImage bi = ImageIO.read(url2); 
				  ImageIO.write(bi, fileFormat, saveFile); // ?????? ???? ????, ?????? ??????
			  }
			  /////////////////////////////////////??????////////////////////////////////////////////////////
			  /////////////////////////////////////???? ????////////////////////////////////////////////////////
			
			List<WebElement> detailLink = driver.findElements(By.cssSelector("#listings > ol > li > article > section > div > figure > a"));
			
			List<String> linkList = new ArrayList<String>();
			  for(WebElement link : detailLink) {
				  	if (link.getAttribute("href").length() < 1300) {
				  		linkList.add(link.getAttribute("href"));
					}
			  }
			  System.out.println("linkList : "+linkList);
			
			  /////////////////////////////////???? ????//////////////////////////////
			List<String> detail = new ArrayList<String>();
			for (WebElement list : name) {
				detail.add(list.getText());
			}
			List<String> detail2 = new ArrayList<String>();
			for (WebElement list : addr) {
				detail2.add(list.getText());
			}
			List<String> detail3 = new ArrayList<String>();
			for (WebElement list : price) {
				detail3.add(list.getText().substring(1).replace(",", ""));
			}
			List<String> detail4 = new ArrayList<String>(); 
			for(String saveFileName : imgSrc){
				detail4.add(saveFileName); 
			}
			List<String> detail5 = new ArrayList<String>(); 
			for(WebElement list : detailLink){
				detail5.add(list.getText()); 
			}
			List<String> detail6 = new ArrayList<String>(); 
			for(WebElement list : location){
				detail6.add(list.getText()); 
			}
			List<String> detail7 = new ArrayList<String>(); 
			for(WebElement list : distance){
				detail7.add(list.getText()); 
			}
			List<String> detail8 = new ArrayList<String>(); 
			for(WebElement list : grade){
				detail8.add(list.getText());
			}
			synchronized (driver) {
				driver.wait(1000);
			}
			//System.out.println("detail : " + detail +"detail2 : "+ detail2 +"detail3 : "+ detail3+"detail4 : "+imgSrc);
			for (int j = 0; j < detail.size(); j++) {
				System.out.println("==============" + j + "?? ???? =============");
//				for (int i = 0; i < 9; i++) {
//					System.out.println("t1(????????) :: " + detail.get(i * j));
//					System.out.println("t2(????) :: " + detail2.get(i * j));
//					System.out.println("t3(????) :: " + detail3.get(i * j));
//					System.out.println("t4(??????) :: "+detail4.get(i * j));
//					System.out.println("t5(????) :: "+linkList.get(i*j));
//					System.out.println("t6(????) :: "+detail6.get(i * j));
//					System.out.println("t7(????????) :: "+detail7.get(i * j));
//					System.out.println("t8(????) :: "+detail8.get(i * j));
//				}
				List<Room> roomList = new ArrayList<Room>(); 
				for(int i=0; i<detail.size();i++) { 
				room = new Room();
				room.setRoomName(detail.get(i));
				room.setRoomAddr(detail2.get(i));
				room.setPrice(Integer.parseInt(detail3.get(i)));
				room.setRoomImg(detail4.get(i));
				room.setDetailLink(linkList.get(i));
				room.setLocation(detail6.get(i));
				room.setDistance(detail7.get(i));
				room.setGrade(detail8.get(i));
				
				//???? ?? ?????? ????
				room.setRoomCity(roomCity);
				room.setCheckIn(checkIn);
				room.setCheckOut(checkOut);
				room.setAdultNum(adultNum+adultNum2);
				room.setChildNum(childNum+childNum2);
				room.setRoomNum(roomNum);
				
				roomList.add(room);
				model.addAttribute("roomList",roomList);
				System.out.println("Room :: "+room);
				}
			}
			
			synchronized (driver) {
				driver.wait(1500);

			}
		} catch (IndexOutOfBoundsException ie) {
			System.out.println("IndexOutOfBounds");
			ie.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
		}
		////////////////////////////////////////////////////////////

		return "forward:/view/room/getRoomList.jsp";

	}
	
	@RequestMapping(value = "getRoom" , method = RequestMethod.GET )
	public String getRoom(
			@RequestParam("detailLink") String detailLink , @RequestParam("roomNum")int roomNum,
			@RequestParam("price")int price, @RequestParam("roomCity")String roomCity,
			@RequestParam("checkIn")String checkIn, @RequestParam("checkOut")String checkOut,
			@RequestParam("adultNum")int adultNum , @RequestParam("childNum")int childNum,
			@RequestParam("location")String location ,@RequestParam("distance")String distance,
			@RequestParam("grade")String grade, @RequestParam("roomAddr")String roomAddr,
//			@ModelAttribute("room")Room room,
			Model model ) throws Exception {
		System.out.println("/getRoom : POST");
		Room room = new Room();
		room.getDetailLink();
		System.out.println("room : "+room);
		System.out.println("?? : "+detailLink);
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		
		// Driver SetUp
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreProtectedModeSettings", true);
		driver = new ChromeDriver(options);
		
		try {
			// get page (= ???????????? url?? ???????? ???? ?? request ?? ???? ????)
			driver.get(detailLink);

			System.out.println("new Link ???? ????");
			//????????	
			WebElement name = driver.findElement(By.xpath("//*[@id=\"property-header\"]/div[1]/div[1]/h1"));
			String roomName = name.getText();
			room.setRoomName(roomName);
			//????????
			room.setRoomAddr(roomAddr);
			//????
			room.setPrice(price);
			//????????
			//System.out.println("RoomCity : "+roomCity);
			room.setRoomCity(roomCity);
			//????????
			room.setCheckIn(checkIn);
			//????????
			room.setCheckOut(checkOut);
			//??????
			room.setRoomNum(roomNum);
			//??????
			room.setAdultNum(adultNum);
			//??????
			room.setChildNum(childNum);
			//detailLink
			room.setDetailLink(detailLink);
			//???? ????
			room.setLocation(location);
			//????????
			room.setDistance(distance);
			//????
			room.setGrade(grade);
			//????????????										
			WebElement main = driver.findElement(By.xpath("//*[@id=\"overview-section-4\"]/ul[1]"));
			String mainService = main.getText();
			room.setMainService(mainService);
			
			//???????? ????????
			if (driver.findElement(By.xpath("//*[@id=\"overview-section-5\"]/ul")) != null ) {
				WebElement family = driver.findElement(By.xpath("//*[@id=\"overview-section-5\"]/ul"));
				String familyService = family.getText();
				room.setFamilyService(familyService);
			}
			
			//???? ????
			WebElement sight = driver.findElement(By.xpath("//*[@id=\"overview-section-6\"]/ul"));
			String sights = sight.getText();
			room.setSights(sights);
			//???? ?? ????
			WebElement hotel = driver.findElement(By.xpath("//*[@id=\"amenities\"]/div[2]/div/div[1]/div/div"));
			String hotelInfo = hotel.getText();
			room.setHotelInfo(hotelInfo);
			//???? ?? ????
			WebElement roomIn = driver.findElement(By.xpath("//*[@id=\"amenities\"]/div[2]/div/div[2]/div/div"));
			String roomInfo = roomIn.getText();
			room.setRoomInfo(roomInfo);
			
			model.addAttribute("room",room);
			/////////////////////////////////////??????////////////////////////////////////////////////////
			List<String> detail = new ArrayList<String>();
			for (int i = 0; i < 8; i++) {
			
			  //List<WebElement> roomImg = driver.findElements(By.cssSelector(
					  //"#listings > ol > li > article > section > div > figure > a > img.u-photo.use-bgimage.featured-img-tablet"));
			  WebElement roomImg = driver.findElement(By.cssSelector("#thumb-"+i+" > a"));
			  roomImg.click();
			  System.out.println("roomImg : "+roomImg);
			  synchronized (driver) {
					driver.wait(200);
				}
			  List<WebElement> bigImg = driver.findElements(By.cssSelector("#carousel-container > div.canvas.widget-carousel-enabled > ul > li:nth-child("+(i+1)+")"));
			  System.out.println("bigImg : "+bigImg);
			  List<String> styleList = new ArrayList<String>();
			  for(WebElement down : bigImg) {
				   styleList.add(down.getAttribute("data-desktop"));
			  }
			   List<String> imgSrc = new ArrayList<String>();
				  for(String tempt : styleList) {
					  //int start = tempt.indexOf("(")+2; 
					  //int end = tempt.lastIndexOf(")")-1;
					  //System.out.println("Start : "+start+"End : "+end); 
					  String url =tempt;
					  //System.out.println("URL : "+url);    .substring(start , end); 
			
					  System.out.println("url : "+url);
			
				  String savePath =
						  "C:\\Users\\User\\git\\Euroverse\\ksy\\WebContent\\resources\\images\\roomImg\\";
						  //"C:\\Users\\User\\git\\repository\\Exhibition\\11.Model2MVCShop\\WebContent\\images\\uploadFiles\\";
				  String saveFileName = System.currentTimeMillis()+".jpg";
				  String fileFormat = "jpg";
				  
				  System.out.println(" DOWN ::: " + url); 
				  System.out.println(" SAVE PATH ::: "+ savePath); 
				  System.out.println(" SAVE FILE NAME ::: " + saveFileName);
				  System.out.println(" FILE FORMAT ::: " + fileFormat);
				  File saveFile = new File(savePath + saveFileName);
				  imgSrc.add(saveFileName);
				  //saveImage(down, saveFile, fileFormat);
				  
				  URL url2 = new URL(url); 
				  // ???????? ?? ?????? URL
				  BufferedImage bi = ImageIO.read(url2); 
				  ImageIO.write(bi, fileFormat, saveFile); // ?????? ???? ????, ?????? ??????
				  
			  }
				 
				  for(String FileName : imgSrc){
					  detail.add(FileName); 
					  room.setRoomImg(detail.get(0));
					  System.out.println("detail :"+detail);
					  System.out.println("detail size : "+detail.size());
					  }
					  List<Room> roomList = new ArrayList<Room>(); 
					  for(int b=0; b<detail.size(); b++) {
						  room = new Room();
						  room.setRoomImg(detail.get(b));
						  
						  System.out.println("detail.get : "+detail.get(b));
						  roomList.add(room);
						  model.addAttribute("roomList",roomList);
						  System.out.println("roomList : "+roomList);
					  }
					  synchronized (driver) {
							driver.wait(200);
						}
				/*
				 * for(int b=0; b<detail.size();b++) { room = new Room();
				 * room.setRoomImg(detail.get(b)); roomList.add(room);
				 * model.addAttribute("roomList",roomList);
				 * System.out.println("roomList : "+roomList); }
				 */
				  
		}
//				  String savePath =
//						  "C:\\Users\\User\\git\\Euroverse\\ksy\\WebContent\\resources\\images\\roomImg\\";
//				  String saveFileName = System.currentTimeMillis()+".jpg";
//				  String fileFormat = "jpg";
//				  
//				  System.out.println(" SAVE PATH ::: "+ savePath); 
//				  System.out.println(" SAVE FILE NAME ::: " + saveFileName);
//				  System.out.println(" FILE FORMAT ::: " + fileFormat);
//				  File saveFile = new File(savePath + saveFileName);
//				  //roomImg.add(saveFileName);
//				  //saveImage(down, saveFile, fileFormat);
//				  
//				  URL url2 = new URL(roomImg); 
//				  // ???????? ?? ?????? URL
//				  BufferedImage bi = ImageIO.read(url2); 
//				  ImageIO.write(bi, fileFormat, saveFile); // ?????? ???? ????, ?????? ??????
			  /////////////////////////////////////??????////////////////////////////////////////////////////
			
			
			
//			 for (WebElement list : etc) {
//				 System.out.println("ETC.. :: "+etc);
//				 System.out.println("List :: "+list.getText());
//			 }
			
			synchronized (driver) {
				driver.wait(1500);

			}
		} catch (IndexOutOfBoundsException ie) {
			System.out.println("IndexOutOfBounds");
			ie.printStackTrace();
		} catch (Exception e) {
	
			e.printStackTrace();
	
		} finally {
	
			driver.close();
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
		}
		synchronized (driver) {
			driver.wait(300);
		}
		////////////////////////////////////////////////////////////
	
		return "forward:/view/room/getRoom.jsp";

	}

	
}