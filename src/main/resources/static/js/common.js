// 【原則使用禁止】subumitの処理にしたいときは、この関数を使うことができる
function doSubmit(url) {
	let form = document.getElementById('seatForm')
	form.action = url;
	form.submit();
}

// WEB APIのレスポンスに基づき、jqGridを描画する
function drawGrid(response) {

	// jqGirdの表の件名を作成する
	var gridTitle = '興行一覧';

	// jqGridの列の表示名を作成する
	var colNames = ['id', 'stand', 'event', 'type', 'position', 'place', 'isDeleted', 'createdBy', 'createdAt', 'updatedBy', 'updatedAt'];

	// jqGridの列ごとの詳細設定を行う
	var colModelSettings = [
		{name:'id', width:20, align:'center', sortable:true},
		{name:'stand', width:70, align:'center', sortable:true},
		{name:'event', width:70, align:'center', sortable:true},
		{name:'type', width:70, align:'center', sortable:true},
		{name:'position', width:70, align:'center', sortable:true},
		{name:'place', width:70, align:'center', sortable:true},
		{name:'isDeleted', width:70, align:'center', sortable:true},
		{name:'createdBy', width:70, align:'center', sortable:true},
		{name:'createdAt', width:70, align:'center', sortable:true},
		{name:'updatedBy', width:70, align:'center', sortable:true},
		{name:'updatedAt', width:70, align:'center', sortable:true}
	];

	// WEB APIのレスポンスをJSONとして解析する
	var stringified = JSON.stringify(response);
	var json = JSON.parse(stringified);

	// jqGridを描画する
	$('#seatTable').jqGrid({
		data : json['seatList'], // データ
		datatype : 'local', // データの種類
		colNames : colNames, // 列ヘッダー名(配列)
		colModel : colModelSettings, // 列の各種設定(オブジェクト配列)
		caption : gridTitle, // Gridのタイトル
		pager : 'seatTablePager', // ページャーのID
		rowNum : 20, // 初期表示行数
		rowList : [20, 50, 100], // 行数設定
		multiselect: true, // チェックボックスを付ける
		autowidth: true, // 横幅の自動調整を行う

		// ロード完了後の処理
		loadComplete: function(data){
			console.log(data);
		},

		// 行選択時に走る処理
		onSelectRow: function(rowid, status, e){
			console.log(rowid, status, e);

			// 選択行を取得し、詳細ページにリダイレクトする
			var rowData = $('#seatTable').getRowData(rowid);
			var url = '/getSeatsDetail?id=' + rowData.id;
			location.href = url;
		},

		// 行選択直前に走る処理
		beforeSelectRow: function(rowid, e){
			console.log(rowid, e);
			//return false;
		}
	});
}

// WEB APIのレスポンスに基づき、jqGridを描画する
function redrawGrid(response) {

	// 描画のために既存の表は一度削除する
	$('#seatTable').GridUnload();

	// 表を描画しなおす
	drawGrid(response);
}

// Grid.jsの初期描画
var grid;
function toGrid(response) {

	// WEB APIのレスポンスをJSONとして解析する
	var stringified = JSON.stringify(response);
	var json = JSON.parse(stringified);

	// jqGridの列の表示名を作成する
	var colNames = ['id', 'stand', 'event', 'type', 'position', 'place', 'isDeleted', 'createdBy', 'createdAt', 'updatedBy', 'updatedAt'];

	// Grid.jsによる表を生成する
	grid = new gridjs.Grid({
		columns: colNames, // 列名
		data: json['seatList'], // データ
		sort: true, // ソートする
		pagination: {
			limit: 10
		}, // ページネーション設定
		url: '/api/v1/search', // URL
		style:{
			th:{"color":"navy"},
			td:{"font-size":"9pt"}
		} // デフォルトのスタイルを変更し、文字を少し小さくする
	}).render(document.getElementById('seatTable'));

	// セルクリック時のイベントを設定する
	grid.on('cellClick', (...args) => {
		console.log(args);
	});
}

// Grid.jsの更新
function updateGrid(response) {

	// WEB APIのレスポンスをJSONとして解析する
	var stringified = JSON.stringify(response);
	var json = JSON.parse(stringified);

	// データのみ差し替える
	grid.updateConfig({
		data: json['seatList']
	}).forceRender();
}

$('#btnSearchSeat').on('click', function(event) {

	// テキストボックスの入力内容を検索条件とする
	var tbStand = $('#tbStand').val();
	var tbEvent = $('#tbEvent').val();
	var tbType = $('#tbType').val();
	var tbPosition = $('#tbPosition').val();

	// JSONデータを作成する
	var jsonData = JSON.stringify({
		stand: tbStand,
		event: tbEvent,
		type: tbType,
		position: tbPosition
	});

	// REST APIを呼び出す
	$.ajax({
		type: 'POST',
		url: '/api/v1/search',
		data: jsonData,
		contentType: 'application/json',
		async: true,
		timeout: 10000,
	})
	.done(function(response) {

		// 通信が成功したときの処理
		//redrawGrid(response);
		toGrid(response);
	})
	.fail(function() {
		// 通信が失敗したときの処理
		console.log('#btnSearchSeat failed.');
	})
	.always(function() {
		// 通信が完了したときの処理
		console.log('#btnSearchSeat end.');
	});
});

$('#btnSelectSeat').on('click', function(event) {

	// REST APIを呼び出す
	$.ajax({
		type: 'GET',
		url: '/api/v1/seats',
		contentType: 'application/json',
		async: true,
		timeout: 10000,
	})
	.done(function(response) {

		// 通信が成功したときの処理
		//drawGrid(response);
		updateGrid(response);
	})
	.fail(function() {

		// 通信が失敗したときの処理
		console.log('#btnSelectSeat failed.');
	})
	.always(function() {

		// 通信が完了したときの処理
		console.log('#btnSelectSeat end.');
	});
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
		//redrawGrid(response);
		updateGrid(response);
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
	var len = $('#seatTable').getGridParam('data').length;
	var rowData = $('#seatTable').getGridParam('data')[len - 1];
	var deleteTargetId = rowData.id;
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
		//redrawGrid(response);
		updateGrid(response);
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
