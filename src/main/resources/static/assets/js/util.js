$(function () {
    // 核選方塊全選控制
    $("#chk-all").click(function() {
        if($(this).is(":checked")) {
            $(".chk").prop("checked", true);
        } else {
            $(".chk").prop("checked", false);
        }
    });
});

function doDelete(formName) {
    if (confirm("確定刪除勾選資料?")) {
        document.forms[formName].submit();
    }
}

function showPreviewImg(event, elementId) {
    const file = event.target.files[0];
    const previewImg = document.getElementById(elementId);
    if (file && file.type.match(/^image\//)) {
        const reader = new FileReader();

        // 當檔案讀取完成時，顯示圖片
        reader.onload = function(e) {
            previewImg.src = e.target.result; // 設定圖片的來源
        };
        
        reader.readAsDataURL(file); // 讀取檔案內容
    } else {
        alert("請上傳圖片檔案！");
    }
}