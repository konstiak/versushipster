(function() {
    'use strict';
    angular
        .module('versushipsterApp')
        .factory('Article', Article);

    Article.$inject = ['$resource'];

    function Article ($resource) {
        var resourceUrl =  'api/articles/:id';

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
