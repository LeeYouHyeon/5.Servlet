<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Register.jsp page</h1>
	<form action="/brd/insert" method="post" enctype="multipart/form-data">
		title : <input type="text" name="title" placeholder="title"> <br>
		<c:if test="${ses.id ne null}">
		writer: ${ses.id}
			<input type="hidden" name="writer" value="${ses.id}">
		</c:if>
		<c:if test="${ses.id eq null}">
		writer : <input type="text" name="writer" readonly
				placeholder="writer">
		</c:if>
		<br> content <br>
		<textarea rows="20" cols="30" name="content"></textarea>
		<br>첨부파일 <br> <input type="file" name="imageFile"><br>

		<!-- Submit 버튼을 클릭하면 action의 주소(/brd/insert) 주소로 데이터를 이동 -->
		<button type="submit">Submit</button>
		<button type="reset">Cancel</button>
	</form>
</body>
</html>