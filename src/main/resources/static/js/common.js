function doSubmit(url) {
	let form = document.getElementById("seatForm")
	form.action = url;
	form.submit();
}

$("#btnSelectSeat").on("click", function(event) {

	// ひとまず現状はsubmitで処理ができることを確認する
	doSubmit('/selectSeats');

	// デフォルトの挙動(submit)を抑止し、非同期API呼び出しができるようにする
	//event.preventDerault();

	// ここにREST API呼び出しのコードを記述する
});

$("#btnInsertSeat").on("click", function(event) {

	// ひとまず現状はsubmitで処理ができることを確認する
	doSubmit('/insertSeats');

	// デフォルトの挙動(submit)を抑止し、非同期API呼び出しができるようにする
	//event.preventDerault();

	// ここにREST API呼び出しのコードを記述する
});

$("#btnDeleteSeat").on("click", function(event) {

	// ひとまず現状はsubmitで処理ができることを確認する
	doSubmit('/deleteSeats');

	// デフォルトの挙動(submit)を抑止し、非同期API呼び出しができるようにする
	//event.preventDerault();

	// ここにREST API呼び出しのコードを記述する
});
