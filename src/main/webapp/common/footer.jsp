<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<footer class="tm-bg-gray pt-5 pb-3 tm-text-gray tm-footer">
	<div class="container-fluid tm-container-small">
		<div class="row">
			<div class="col-lg-6 col-md-12 col-12 px-5 mb-5">
				<h3 class="tm-text-primary mb-4 tm-footer-title">About WatchNet</h3>
				<p>WatchNet là một website trực tuyến có đầy đủ các loại video, 
				từ video ngắn, video giáo dục đến phim và ca nhạc. 
				Với hàng ngàn nội dung được cập nhật hàng ngày.</p>
			</div>
			<div class="col-lg-3 col-md-6 col-sm-6 col-12 px-5 mb-5">
				<h3 class="tm-text-primary mb-4 tm-footer-title">Liên kết</h3>
				<ul class="tm-footer-links pl-0">
					<li><a href="#">Quảng cáo</a></li>
					<li><a href="#">Hỗ trợ</a></li>
					<li><a href="#">Công ty chúng tôi</a></li>
					<li><a href="#">Liên hệ</a></li>
				</ul>
			</div>
			<div class="col-lg-3 col-md-6 col-sm-6 col-12 px-5 mb-5">
				<ul class="tm-social-links d-flex justify-content-end pl-0 mb-5">
					<li class="mb-2"><a href="https://facebook.com"><i
							class="fab fa-facebook"></i></a></li>
					<li class="mb-2"><a href="https://twitter.com"><i
							class="fab fa-twitter"></i></a></li>
					<li class="mb-2"><a href="https://instagram.com"><i
							class="fab fa-instagram"></i></a></li>
					<li class="mb-2"><a href="https://pinterest.com"><i
							class="fab fa-pinterest"></i></a></li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div
				class="col-lg-8 col-md-7 col-12 px-5 mb-3 offset-lg-4 offset-md-5 ">Copyright 
				2024 Nguyen Van Giau © WatchNet. All rights reserved.</div>		
		</div>
	</div>
</footer>

<script src="<c:url value='templates/user/js/plugins.js'/>"></script>
<script>
	$(window).on("load", function() {
		$('body').addClass('loaded');
	});
</script>
<script>
	$('#changePassBtn').click(function(){
		$('#messageChangePass').text('');
		var currentPass = $('#currentPass').val();
		var newPass = $('#newPass').val();
		var formData = {
						'currentPass' : currentPass,
						'newPass' : newPass,
						};
		$.ajax({
			url : 'changePass',
			type: 'POST',
			data: formData,
		}).then(function(data) {
			$('#messageChangePass').text('ChangePass susscessfully');
		}).fail(function(error) {
			$('#messageChangePass').text('ChangePass faild, Pleasee check your information!');
		});
	});
</script>