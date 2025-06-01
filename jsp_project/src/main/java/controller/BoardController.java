package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.MemberVO;
import domain.PagingVO;
import handler.FileRemoveHandler;
import handler.PagingHandler;
import net.coobird.thumbnailator.Thumbnails;
import service.BoardService;
import service.BoardServiceImpl;

/**
 * Servlet implementation class BoardController
 */
@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 로그 객체 생성
	private static final Logger Log = LoggerFactory.getLogger(BoardController.class);

	// jsp에서 받은 요청을 처리하고 그 결과를 다른 jsp로 보내주는 객체
	private RequestDispatcher rdp;
	// 목적지(jsp) 주소를 저장하는 변수
	private String destPage;
	// isOk 변수
	private int isOk;

	// service 연결 인터페이스
	private BoardService bsv;

	public BoardController() {
		// 생성자 => bsv를 구현 객체로 연결해 생성
		bsv = new BoardServiceImpl();
	}

	// 여기선 service가 doGet과 doPost를 전부 처리하도록 구성
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 모든 처리는 service에서
		Log.info("BoardController test");

		// 들어오는 매개변수와 응답객체의 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 동기 방식 : 응답 객체의 content Type을 text/html로 설정
		response.setContentType("text/html; charset=UTF-8");

		// jsp에서 요청하는 주소를 받는 객체
		String uri = request.getRequestURI();
		Log.info(uri); // /brd/register => register

		String path = uri.substring(uri.lastIndexOf("/") + 1); // register만 추출
		Log.info(path);

		switch (path) {
		case "register":
			destPage = "/board/register.jsp";
			break;
		case "insert":
			try {
				// 파일 첨부가 있을 경우 사용
				// 파일 업로드 시 사용할 물리적인 경로를 설정
				String savePath = getServletContext().getRealPath("/_fileUpload");
				Log.info(">>> savePath >> {}", savePath);

				// File 객체 생성
				File fileDir = new File(savePath);
				Log.info(">>> fileDir >> {}", fileDir);

				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				// 파일 저장 객체 (파일 경로)
				fileItemFactory.setRepository(fileDir);
				// 파일 저장시 사용할 메모리 공간
				fileItemFactory.setSizeThreshold(1024 * 1024 * 3); // 계산 가능

				BoardVO bvo = new BoardVO();

				// request에서 multipart/form-data 형식으로 넘어온 객체를 다루기 쉽게 변환해주는 클래스
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);

				List<FileItem> itemList = fileUpload.parseRequest(request);

				for (FileItem item : itemList) {
					// item -> title, writer, content, imageFile
					switch (item.getFieldName()) {
					case "title":
						bvo.setTitle(item.getString("UTF-8")); // 바이트 형태로 풀어진 값을 UTF-8로 재조합
						break;
					case "writer":
						bvo.setWriter(item.getString("UTF-8"));
						break;
					case "content":
						bvo.setContent(item.getString("UTF-8"));
						break;
					case "imageFile":
						// value가 없다고 null이 뜨지 않음. null이 뜨려면 웹에서 그 항목 자체가 없어야 함
						// 용량으로 이미지 파일 존재 여부를 체크
						if (item.getSize() > 0) {
							// 파일 이름 추출
							// getName() : 경로를 포함하는 이름
							String fileName = item.getName().substring(item.getName().lastIndexOf(File.separator) + 1);
							// File.separator : 파일 경로 기호 => 운영체제마다 다를 수 있어서 자동 변환
							Log.info(">>>> fileName >> {}", fileName);
							// 시스템의 시간을 이용하여 파일 구분 시간_파일이름
							// UUID UUID_파일이름
							fileName = System.currentTimeMillis() + "_" + fileName;

							// 경로 + 구분자 + 파일이름
							File uploadFile = new File(fileDir + File.separator + fileName);
							Log.info(">>> uploadFile >> {}", uploadFile);

							// 저장
							try {
								item.write(uploadFile); // 파일 객체에 실제 데이터 쓰기
								bvo.setImageFile(fileName); // 프로젝트 내부에 저장된 이미지는 /로 연결 가능

								// 썸네일 작업 : 리스트 페이지에서 트래픽 과다 사용 방지 (연결시간 지연 방지)
								Thumbnails.of(uploadFile).size(75, 75)
										.toFile(new File(fileDir + File.separator + "_th_" + fileName));
							} catch (Exception e) {
								// TODO: handle exception
								Log.info("file writer on disk error");
								e.printStackTrace();
							}
						}
						break;
					}
				}

				Log.info(">> bvo {}", bvo);
				isOk = bsv.insert(bvo);
				Log.info(">> insert {}", isOk > 0 ? "성공" : "실패");

				// 파일 첨부가 없을 때 사용. enctype="multipart/form-data"가 아닐 경우 사용
				// request로 받은 데이터를 추출
//				String title = request.getParameter("title"), writer = request.getParameter("writer"),
//						content = request.getParameter("content");
//
//				// DB에 넣기 위해 객체 생성
//				BoardVO bvo = new BoardVO(title, writer, content);
//				

				// boardService 객체로 해당 객체를 전달
				//

				// DB에서 저장이 완료되면 1, 안 되면 0이 리턴

				destPage = "/index.jsp";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		case "list":
			try {
				// DB에서 전체 리스트를 요청
//				List<BoardVO> list = bsv.getList();

				PagingVO pgvo = new PagingVO(); // pageNo = 1, qty = 10;

				if (request.getParameter("pageNo") != null) {
					pgvo.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
					pgvo.setQty(Integer.parseInt(request.getParameter("qty")));
					pgvo.setType(request.getParameter("type"));
					pgvo.setKeyword(request.getParameter("keyword"));
				}

				// select * from board order by bno desc limit pageStart, qty;
				// select * from board where type like "%keyword%" ...

				List<BoardVO> list = bsv.getList(pgvo);
				Log.info(">> list {}", list);

				int totalCount = bsv.getTotal(pgvo);
				Log.info(">> totalCount > " + totalCount);

				// 페이지 핸들러 객체 생성 => 화면으로 보내기
				PagingHandler ph = new PagingHandler(pgvo, totalCount);
				Log.info(" >> ph > {}", ph);

				// request의 객체로 값을 보내는 역할
				request.setAttribute("list", list);
				request.setAttribute("ph", ph);

				destPage = "/board/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "detail":
		case "modify":
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));

				BoardVO bvo = bsv.getDetail(bno);
				request.setAttribute("bvo", bvo);
				Log.info(">> detail bvo > {} ", bno);
				destPage = String.format("/board/%s.jsp", path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "update":
			try {
				// 파일 업로드 포함 시
				String savePath = getServletContext().getRealPath("_fileUpload");
				File fileDir = new File(savePath);

				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir);
				fileItemFactory.setSizeThreshold(1024 * 1024 * 3);

				BoardVO bvo = new BoardVO();
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);

				List<FileItem> itemList = fileUpload.parseRequest(request);

				String old_file = null; // 기존에 있던 imageFile

				for (FileItem item : itemList) {
					switch (item.getFieldName()) {
					case "bno":
						bvo.setBno(Integer.parseInt(item.getString("UTF-8")));
						break;
					case "title":
						bvo.setTitle(item.getString("UTF-8"));
						break;
					case "content":
						bvo.setContent(item.getString("UTF-8"));
						break;
					case "imageFile":
						// 기존 파일 => 있을 수도 있고 없을 수도 있음
						old_file = item.getString("UTF-8");
						break;
					case "newFile":
						// 새로 추가되는 파일 => 있을 수도 있고 없을 수도 있음
						if (item.getSize() > 0) {
							// 새로 추가되는 파일이 있을 경우
							if (old_file != null) {
								// old_file 삭제 작업
								// fileRemoveHandler를 통해 파일 삭제 작업을 진행
								FileRemoveHandler removeHandler = new FileRemoveHandler();
								isOk = removeHandler.deleteFile(savePath, old_file);
							}
							// 새로운 파일 등록
							String fileName = item.getName().substring(item.getName().lastIndexOf(File.separator) + 1);
							Log.info(">>>> new File >> {}", fileName);
							fileName = System.currentTimeMillis() + "_" + fileName;
							File uploadFile = new File(fileDir + File.separator + fileName);

							try {
								item.write(uploadFile);
								bvo.setImageFile(fileName);

								Thumbnails.of(uploadFile).size(75, 75)
										.toFile(fileDir + File.separator + "_th_" + fileName);
							} catch (Exception e) {
								Log.info("file writer update Error");
								e.printStackTrace();
							}
						} else {
							// 기존 파일을 bvo에 담음
							bvo.setImageFile(old_file);
						}
					}
				}
				Log.info(">> update bno > {}", bvo);
				int isOk = bsv.update(bvo);
				Log.info(">> update {}", isOk > 0 ? "성공" : "실패");
				destPage = "detail?bno=" + bvo.getBno();
//				destPage = "detail"; : 이전엔 리퀘스트가 비워지지 않아서 꼼수로 bno를 추출할 수 있던 것
				// destPage = "list";

//				int bno = Integer.parseInt(request.getParameter("bno"));
//				String title = request.getParameter("title"), content = request.getParameter("content");
//				BoardVO bvo = new BoardVO(bno, title, content);
//				Log.info(">> update bno > {}", bno);
//
//				isOk = bsv.update(bvo);
//				Log.info(">> update {}", isOk > 0 ? "성공" : "실패");
//				destPage = "detail"; // 내부 케이스를 한 번 더 돌아감
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "remove":
			try {
				// 1. bno를 가지고 BoardVO 객체 생성
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				Log.info(">>>> bvo >> {}", bvo);

				// 2. 글, 댓글 삭제
				isOk = bsv.remove(bno);
				Log.info(">> remove bvo {} > {}", bno, isOk > 0 ? "성공" : "실패");

				// 3. 글이 삭제됐으면 이미지 삭제
				if (isOk > 0) {
					if (bvo.getImageFile() != null) {
						FileRemoveHandler removeHandler = new FileRemoveHandler();
						isOk = removeHandler.deleteFile(getServletContext().getRealPath("/_fileUpload"),
								bvo.getImageFile());
						Log.info(">>>> remove image file >> {}", isOk > 0 ? "성공" : "실패");
					} else
						Log.info(">>>> no image on this board");
				}

				destPage = "list";
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		case "mylist":
			try {
				// DB에서 전체 리스트를 요청
//				List<BoardVO> list = bsv.getList();

				PagingVO pgvo = new PagingVO(); // pageNo = 1, qty = 10;

				if (request.getParameter("pageNo") != null) {
					pgvo.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
					pgvo.setQty(Integer.parseInt(request.getParameter("qty")));
					pgvo.setType(request.getParameter("type"));
					pgvo.setKeyword(request.getParameter("keyword"));
				}

				// select * from board order by bno desc limit pageStart, qty;
				// select * from board where type like "%keyword%" ...
				
				String id = ((MemberVO)request.getSession().getAttribute("ses")).getId();
				Log.info(">> board of id {}", id);
				
				HashMap<String, Object> map = new HashMap<>();
				map.put("id", id);
				map.put("pgvo", pgvo);
				
				List<BoardVO> list = bsv.getList(map);
				Log.info(">> list {}", list);

				int totalCount = bsv.getTotal(map);
				Log.info(">> totalCount > " + totalCount);

				// 페이지 핸들러 객체 생성 => 화면으로 보내기
				PagingHandler ph = new PagingHandler(pgvo, totalCount);
				Log.info(" >> ph > {}", ph);

				// request의 객체로 값을 보내는 역할
				request.setAttribute("list", list);
				request.setAttribute("ph", ph);
			} catch (Exception e) {
				e.printStackTrace();
			}
			destPage = "/board/mylist.jsp";
			break;
		}

		// 받은 주소에 대한 분기 처리

		// 처리가 완료되고 만들어진 응답객체를 jsp 화면으로 보내기
		// 데이터 전달 객체 (RequestDispatcher) / 전달 jsp (destPage)
		rdp = request.getRequestDispatcher(destPage);
		// 요청한 객체를 가지고 destPage에 적힌 주소로 이동 forward
		rdp.forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

}
