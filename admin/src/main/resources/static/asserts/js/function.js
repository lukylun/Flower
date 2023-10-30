
/**
 * 데이터 조회
 * @param uri - API Request URI
 * @param params - Parameters
 * @returns json - 결과 데이터
 */


function getJson(uri, params) {

    let json = {}

    $.ajax({
        url : uri,
        type : 'get',
        dataType : 'json',
        data : params,
        async : false,
        success : function (response) {
            json = response;
        },
        error : function (request, status, error) {
            console.log(error)
        }
    })

    return json;
}


/**
 * 데이터 저장/수정/삭제
 * @param uri - API Request URI
 * @param method - API Request Method
 * @param params - Parameters
 * @returns json - 결과 데이터
 */
function callApi(uri, method, params) {

    let json = {}

    $.ajax({
        url : uri,
        type : method,
        contentType : 'application/json; charset=utf-8',
        dataType : 'json',
        data : (params) ? JSON.stringify(params) : {},
        async : false,
        success : function (response) {
            json = response;
        },
        error : function (request, status, error) {
            console.log(error)
        }
    })

    return json;
}