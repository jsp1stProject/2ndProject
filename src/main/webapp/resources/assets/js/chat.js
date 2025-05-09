
$(function () {
    $(".chattoggler").on("click", function () {
        let tg_attr = $(this).attr("data-target");
        let tg = document.querySelector(`#${tg_attr}`)
        $(tg).toggleClass("active");
        // if ($("#main-wrapper").hasClass("mini-sidebar")) {
        //     $(".sidebartoggler").prop("checked", !0);
        //     $("#main-wrapper").attr("data-sidebartype", "mini-sidebar");
        // } else {
        //     $(".sidebartoggler").prop("checked", !1);
        //     $("#main-wrapper").attr("data-sidebartype", "full");
        // }
    });
    $(".chatclose").on("click", function(){
        let tg_attr = $(this).attr("data-target");
        let tg = document.querySelector(`#${tg_attr}`)
        $(tg).removeClass("active");
    });
    // $(".sidebartoggler").on("click", function () {
    //     $("#main-wrapper").toggleClass("show-sidebar");
    // });
})