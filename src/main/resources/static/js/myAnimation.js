$('.menu.toggle').click(function () {
    // $('.m-item').toggleClass('autumn');
    // $('.m-item').toggleClass('leaf');
    $('.autumn.leaf').transition('fly down');
    $('#sidebar-icon').transition('jiggle');
    $('.m-item').toggleClass('m-mobile-hide');
});