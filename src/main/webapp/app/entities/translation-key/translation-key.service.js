(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('TranslationKey', TranslationKey);

    TranslationKey.$inject = ['$resource'];

    function TranslationKey ($resource) {
        var resourceUrl =  'api/translation-keys/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
