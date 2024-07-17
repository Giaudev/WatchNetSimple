<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register Account</title>
<%@ include file="/common/head.jsp"%>
</head>
<body>
	<%@ include file="/common/header.jsp"%>

	<div class="container-fluid tm-mt-60">
        <div class="row tm-mb-50">
            <div class="col-lg-4 col-12 mb-5 offset-lg-4">
                <h2 class="tm-text-primary mb-5 text-center">Register</h2>
                <form id="register-form" action="register" method="POST" class="tm-contact-form mx-auto">
                    <div class="form-group">
                        <input type="text" name="username" class="form-control rounded-0" placeholder="Username" required />
                    </div>
                    <div class="form-group">
                        <input type="password" name="password" class="form-control rounded-0" placeholder="Password" required />
                    </div>
                    <div class="form-group">
                        <input type="password" name="cfmpassword" class="form-control rounded-0" placeholder="Confirm Password" required />
                    </div>
                    <div class="form-group">
                        <input type="email" name="email" class="form-control rounded-0" placeholder="Address Email" required />
                    </div>
                    <div class="form-group tm-text-right">
                        <button type="submit" class="form-control btn btn-primary " style="color: white !important;">Register</button>
                    </div>
                </form>                
            </div>    

	<%@ include file="/common/footer.jsp"%>
</body>
</html>