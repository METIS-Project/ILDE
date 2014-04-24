$(document).ready(function()
{

    //Search input placeholder (remove when HTML5 works in IE!)
    /*
     $('#ldshake_topbar_search_input')
     .val($('#ldshake_topbar_search_input').attr('placeholder'))
     .focus (function ()
     {
     if ($(this).val() == $(this).attr('placeholder'))
     {
     $(this).val('');
     $(this).addClass('written');
     }
     })
     .blur (function ()
     {
     if ($(this).val() == '')
     {
     $(this).removeClass('written');
     $(this).val($(this).attr('placeholder'));
     }
     });
     */

    //Topbar account options
    $('#ldshake_topbar_menu_switch').click (function (e)
    {
        $('#ldshake_topbar_user_menu').css('left', $('#layout_canvas').offset().left + $('#layout_canvas').width() - $('#ldshake_topbar_user_menu').width() - 20);
        $('#ldshake_topbar_user_menu').fadeToggle(200);
        $('#ldshake_topbar_user_options').toggleClass('menu_active');
        e.preventDefault();
        e.stopPropagation();
    });

    $('#tb_newlds').click (function (e)
    {
        $('#toolbar_lds_types').fadeToggle(200);
        $('#tb_newlds a').toggleClass('menu_active');
        e.preventDefault();
        e.stopPropagation();
    });

    $('#toolbar_lds_types').mouseenter(function(){
        $(this).find('li')
            .css('background-color', '')
            .css('color', '');
    });

    var menu_names = ["conceptualize", "author", "implement", "project"];

    for(var i=0; i<menu_names.length; i++) {
        (function(index) {
            var menu_name = menu_names[i];
            $('#tb_new_option_'+menu_name).mouseenter(function (e)
            {
                var menuOffsetX = $(e.target.parentElement.parentElement).position().left;
                var menuOffsetY = $(e.target.parentElement.parentElement).position().top;
                var $target = $(e.target);

                $('#new_menu_'+menu_name)
                    .css('top', $target.position().top + menuOffsetY + 5)
                    .css('left', $target.outerWidth() + menuOffsetX - 5)
                    .fadeIn(200);
            }).mouseleave(function (e)
                {
                    var $nm = $('#new_menu_'+menu_name);

                    var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
                    var evalY = (e.pageX >= $nm.offset().left - 1 && e.pageX <= $nm.offset().left + $nm.outerWidth());

                    if(!evalX || !evalY) {
                        $nm.fadeOut(200);
                    } else {
                        $(this)
                            .css('background-color', 'rgb(56, 99, 47)')
                            .css('color', '#fff');
                    }
                });

            $('#new_menu_'+menu_name).mouseleave(function (e)
            {
                var $nm = $('#tb_new_option_'+menu_name);
                var evalX = (e.pageY >= $nm.offset().top && e.pageY <= $nm.offset().top + $nm.outerHeight());
                var evalY = (e.pageX >= $nm.offset().left && e.pageX <= $nm.offset().left + $nm.outerWidth());

                if(!evalX || !evalY) {
                    $(this).fadeOut(200);
                    $nm
                        .css('background-color', '')
                        .css('color', '');
                }
            });
        })(i)
    }

    $('body').click(function() {
        $('#toolbar_lds_types').fadeOut(200);
        $('.menu').fadeOut(200);
        $('#tb_newlds a').removeClass('menu_active');

        $('#ldshake_topbar_user_menu').hide();
        $('#ldshake_topbar_user_options').removeClass('menu_active');
    });

    //Submit search form when clicked the icon

    $('#ldshake_topbar_search_submit').click (function ()
    {
        $('#searchform').submit();
        return false;
    });


});