
$(document).ready(function () {
    // Seleciona o header e os itens de navegação
    const header = $('header');
    const navItems = $('.nav-item');

    // Efeito de sombra 
    $(window).on('scroll', function () {
        const scrollPosition = $(window).scrollTop();

        if (scrollPosition <= 0) {
            header.css('box-shadow', 'none');
        } else {
            header.css('box-shadow', '5px 1px 5px rgba(0, 0, 0, 0.1)');
        }
    });

    // Muda o active somente ao clicar
    navItems.on('click', function () {
        navItems.removeClass('active');
        $(this).addClass('active');
    });
});

