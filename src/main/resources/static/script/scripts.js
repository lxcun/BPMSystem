(function ($) {
    $(document).ready(function () {
        /*==Left Navigation Accordion ==*/ /* 左边导航栏手风琴效果*/
        if ($.fn.dcAccordion) {
            $('#menuid').dcAccordion({
                eventType: 'click',
                autoClose: true,
                saveState: true,
                disableLink: true,
                speed: 'slow',
                showCount: false,
                autoExpand: true,
               // classExpand: 'dcjq-current-parent'
            });
        }
        /*==Slim Scroll ==*/

        /*==Nice Scroll ==*/

        /*==Easy Pie chart ==*/

        /*== SPARKLINE==*/
        

        /*==Sidebar Toggle==*/
 
    });


})(jQuery);