 function connect() {
		socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);
		stompClient.connect({ 'roomId' : roomId }, stompSuccess, stompFailure);
	}
	
	function stompSuccess(frame) {
		enableInputMessage();
		//successMessage("Your WebSocket connection was successfuly established!")
		
		stompClient.subscribe('/room/connected.users', updateConnectedUsers);
		stompClient.subscribe('/room/old.messages', oldMessages);
		
		stompClient.subscribe('/topic/' + roomId + '.public.messages', publicMessages);
		stompClient.subscribe('/user/queue/' + roomId + '.private.messages', privateMessages);
		stompClient.subscribe('/topic/' + roomId + '.connected.users', updateConnectedUsers);
	}
	
	function stompFailure(error) {
		errorMessage("Lost connection to WebSocket! Reconnecting in 10 seconds...");
		disableInputMessage();
	    setTimeout(connect, 10000);
	}
	
	function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect();
		}
		window.location.href = "/chat";
	}
	
	function updateConnectedUsers(response) {
		var connectedUsers = JSON.parse(response.body);
		var $tbody = $("tbody").html("");
		
		$.each(connectedUsers, function(index, connectedUser) {
			var $tr = $("<tr />");
			var $td = $('<td />', {
				"class" : "users",
				"text" : connectedUser.username
			});
			$td.appendTo($tr);
			$tr.appendTo($tbody);
		});
		
		bindConnectedUsers();
	}
	
	function oldMessages(response) {
		var instantMessages = JSON.parse(response.body);
		
		$.each(instantMessages, function(index, instantMessage) {
			if (instantMessage.public == true) {
				appendPublicMessage(instantMessage);
			} else {
				appendPrivateMessage(instantMessage);
			}
		});
		
		scrollDownMessagesPanel();
	}
	
	function publicMessages(message) {
		var instantMessage = JSON.parse(message.body);
		appendPublicMessage(instantMessage);
		scrollDownMessagesPanel();
	}
	
	function appendPublicMessage(instantMessage) {
		if (instantMessage.fromUser == "admin") {
			newMessages
			.append('<p class="alert alert-warning"><strong>' + instantMessage.fromUser + '</strong>: ' + 
					instantMessage.text + '</p>')
		} else {
			newMessages
				.append("<p>" + instantMessage.fromUser + ": " + instantMessage.text + "</p>")
		}
	}
	
	function privateMessages(message) {
		var instantMessage = JSON.parse(message.body);
		appendPrivateMessage(instantMessage);
		scrollDownMessagesPanel();
	}
	
	function appendPrivateMessage(instantMessage) {
		newMessages
		.append('<p class="alert alert-info">[private] ' + 
				'<strong>' + instantMessage.fromUser + 
				' <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span> ' + 
				instantMessage.toUser + '</strong>: ' + 
				instantMessage.text + '</p>');
	}
	
	function sendMessage() {
		var instantMessage;
		
		if (inputMessageIsEmpty()) {
			inputMessage.focus();
			return;
		}

		if (spanSendTo.text() == "public") {
			instantMessage = {
				'text' : inputMessage.val()
			}
		} else {
			instantMessage = {
				'text' : inputMessage.val(),
				'toUser' : spanSendTo.text()
			}
		}
		stompClient.send("/chatroom/send.message", {}, JSON.stringify(instantMessage));
		inputMessage.val("").focus();
	}
	
	function inputMessageIsEmpty() {
		return inputMessage.val() == "";
	}

	function sendTo(e) {
		spanSendTo.text(e.toElement.textContent);
		inputMessage.focus();
	}
	
	function checkEnter(e) {
		var key = e.which;
		if(key == 13) {
		   btnSend.click();
		   return false;  
		}
	}
	
	function scrollDownMessagesPanel() {
		newMessages.animate({"scrollTop": newMessages[0].scrollHeight}, "fast");
	}
	
	function bindConnectedUsers() {
		$(".users").on("click", sendTo);
	}
	
	function enableInputMessage() {
		inputMessage.prop("disabled", false);
	}
	
	function disableInputMessage() {
		inputMessage.prop("disabled", true);
	}
	
	function successMessage(msg){
        noty({
            text: msg,
            layout: 'top',
            type: 'success',
            timeout: 5000
        });
	}
	
	function errorMessage(msg){
        noty({
            text: msg,
            layout: 'top',
            type: 'error',
            timeout: 5000
        });
	}