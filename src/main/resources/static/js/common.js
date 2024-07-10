function doSubmit(url) {
	let form = document.getElementById('seatForm')
	form.action = url;
	form.submit();
}

$('#btnSelectSeat').on('click', function(event) {

	// GET処理はサブミットを行い、初期画面を表示する
	doSubmit('/selectSeats');
});

$('#btnInsertSeat').on('click', function(event) {

	// JSONデータを作成する
	var jsonData = JSON.stringify({
		stand: 'stand_001',
		event: 'event_001',
		type: 'type_001',
		position: 'position_001',
		place: 'place_001'
	});

	// REST APIを呼び出す
	$.ajax({
		type: 'POST',
		url: '/api/v1/seats',
		data: jsonData,
		contentType: 'application/json',
		async: true,
		timeout: 10000,
	})
	.done(function(response) {

		// 通信が成功したときの処理
		// WEB APIが返却したJSON値を元に、tableのtbodyを更新する
		var stringified = JSON.stringify(response);
		var json = JSON.parse(stringified);
		var tableBodyHtml = '<tbody id="seatTableBody">';
		for (let i = 0; i < json['seatList'].length; i++) {
			tableBodyHtml += '\n<tr>';
			tableBodyHtml += '<td>' + json['seatList'][i]['id'] + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i]['stand'] + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].event + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].type + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].position + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].place + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].isDeleted + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].createdBy + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].createdAt + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].updatedBy + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].updatedAt + '</td>';
			tableBodyHtml += '</tr>';
		}
		tableBodyHtml += '\n</tbody>';
		$('#seatTableBody').html(tableBodyHtml);
	})
	.fail(function() {
		// 通信が失敗したときの処理
		console.log('#btnInsertSeat failed.');
	})
	.always(function() {
		// 通信が完了したときの処理
		console.log('#btnInsertSeat end.');
	});
});

$('#btnDeleteSeat').on('click', function(event) {

	// リクエストパラメータを含むURLを作成する
	var tableTr = $('#seatTable tr');
	var deleteTargetId = 0;
	for (var indexTr = 0; indexTr < tableTr.length; indexTr++) {
		var cells = tableTr.eq(indexTr).children();
		for (var indexCell = 0; indexCell < cells.length; indexCell++) {
			if (indexTr === tableTr.length - 1 && indexCell === 0) {
				deleteTargetId = cells.eq(indexCell).text();
				break;
			}
		}
	}
	var urlWithParam = '/api/v1/seats?id=' + deleteTargetId;

	// REST APIを呼び出す
	$.ajax({
		type: 'DELETE',
		url: urlWithParam,
		contentType: 'application/json',
		async: true,
		timeout: 10000,
	})
	.done(function(response) {

		// 通信が成功したときの処理
		// WEB APIが返却したJSON値を元に、tableのtbodyを更新する
		var stringified = JSON.stringify(response);
		var json = JSON.parse(stringified);
		var tableBodyHtml = '<tbody id="seatTableBody">';
		for (let i = 0; i < json['seatList'].length; i++) {
			tableBodyHtml += '\n<tr>';
			tableBodyHtml += '<td>' + json['seatList'][i]['id'] + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i]['stand'] + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].event + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].type + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].position + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].place + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].isDeleted + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].createdBy + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].createdAt + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].updatedBy + '</td>';
			tableBodyHtml += '<td>' + json['seatList'][i].updatedAt + '</td>';
			tableBodyHtml += '</tr>';
		}
		tableBodyHtml += '\n</tbody>';
		$('#seatTableBody').html(tableBodyHtml);
	})
	.fail(function() {
		// 通信が失敗したときの処理
		console.log('#btnDeleteSeat failed.');
	})
	.always(function() {
		// 通信が完了したときの処理
		console.log('#btnDeleteSeat end.');
	});
});
