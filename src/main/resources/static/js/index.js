// 为id是publishBtn的绑定单击事件
$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	//发送ajax请求之前 将csrf令牌设置到请求的消息头中
	// var token =$("meta[name='_csrf']").attr("content");
	// var header=$("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function (e, xhr, options) {
	// 	xhr.setRequestHeader(header,token);
	// });
	//获取标题和内容
	var title=$("#recipient-name").val();
	var content=$("#message-text").val();
	//发送异步请求
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		//回调函数
		function (data) {
			//将JSON字符串转为与之对应的JavaScript对象。
			data=$.parseJSON(data);
			//在提示框中显示返回的消息
			$("#hintBody").text(data.msg);

			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code==0){
					//code==0说明成功
					window.location.reload();
				}
			}, 2000);
		}
	);


}