    $('#payButton').popup({
        popup: $('.payQR.popup'),
        on: 'click',
        position: 'bottom center'
    })

    tocbot.init({
        // Where to render the table of contents.
        tocSelector: '.js-toc',
        // Where to grab the headings to build the table of contents.
        contentSelector: '.js-toc-content',
        // Which headings to grab inside of the contentSelector element.
        headingSelector: 'h1, h2, h3',
        // For headings inside relative or absolute positioned containers within content.
        hasInnerContainers: true,
    });

    $('#tocBtn').popup({
        popup: $('.tocPu.popup'),
        on: 'click',
        position: 'left center'
    });
    $('#wechatBtn').popup({
        popup: $('.wechatQR'),
        position: 'left center'
    });


    var waypoint = new Waypoint({
        element: document.getElementById('waypoint'),
        handler: function(direction) {
            if (direction == 'down') {
                $('#toolbar').show(300);
            } else {
                $('#toolbar').hide(500);
            }
            console.log('Scrolled to waypoint!  ' + direction);
        }
    });


    $('.ui.form').form({
        fields: {
            nickname: {
                identifier: 'nickname',
                rules: [{
                    type: 'empty',
                    prompt: '昵称：请输入昵称'
                }]
            },
            email: {
                identifier: 'email',
                rules: [{
                         type: 'email',
                         prompt: '邮箱：请正确输入您的邮箱（用于接收回复信息）'
                }]
            },
            content: {
                identifier: 'content',
                rules: [{
                    type: 'empty',
                    prompt: '内容：请输入回复内容'
                }]
            }
        }
    });


    $('.fancy-box-img').each(function() {
        var element = document.createElement('a');
        $(element).attr('data-fancybox', 'gallery');
        $(element).attr('href', $(this).attr('src'));
        $(this).wrap(element);
    });

        $('title').html($('#blog-title').text());
        window.onfocus = function () {
            $('title').html($('#blog-title').text());
        };
        window.onblur = function () {
            $('title').html('震惊！该页面出现不可描述信息！');
        };

