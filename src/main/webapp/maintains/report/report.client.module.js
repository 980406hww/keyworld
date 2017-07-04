(function (app) {
    app.registerModule('ReportManagement', [
        'toaster',
        'ngAnimate',
        'ngSanitize',
        'ngCookies',
        'ui.select',
        'ui.bootstrap',
        'ui.grid',
        'ui.grid.cellNav',
        'ui.grid.resizeColumns',
        'ui.grid.selection',
        'ui.grid.autoResize',
        'ui.grid.pagination',
        'UserInfoManagement'
    ]);
}(ApplicationConfiguration));