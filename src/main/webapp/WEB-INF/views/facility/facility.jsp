<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>시설 지도</title>
    <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b72d19d574bc9065c96bcc5bdc881fc0&libraries=services"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
    <h1>내 근처 시설 확인하기</h1>
    <!-- 주소 입력 -->
    <input type="text" id="userAddress" placeholder="주소를 입력하세요" onclick="execDaumPostcode()" readonly style="width:300px;">
    <button onclick="findNearby()">내 근처 시설 보기</button>

    <div id="map" style="width:100%; height:400px; margin-top:10px;"></div>

    <script>
        let map; // 지도 객체
        let userLat, userLon;

        function execDaumPostcode() {
            new daum.Postcode({
                oncomplete: function(data) {
                    document.getElementById('userAddress').value = data.address;
                    addressToCoords(data.address, function(lat, lon) {
                        userLat = lat;
                        userLon = lon;
                    });
                }
            }).open();
        }

        function addressToCoords(address, callback) {
            var geocoder = new kakao.maps.services.Geocoder();
            geocoder.addressSearch(address, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    var lat = parseFloat(result[0].y);
                    var lon = parseFloat(result[0].x);
                    callback(lat, lon);
                } else {
                    alert("주소를 찾을 수 없습니다.");
                }
            });
        }

        function findNearby() {
            if (!userLat || !userLon) {
                alert("주소를 입력해주세요.");
                return;
            }

            fetch(`/nearbyFacilities?lat=${userLat}&lon=${userLon}`)
                .then(res => res.json())
                .then(data => {
                    displayMap(userLat, userLon, data);
                });
        }

        function displayMap(centerLat, centerLon, facilities) {
            var container = document.getElementById('map');
            var options = {
                center: new kakao.maps.LatLng(centerLat, centerLon),
                level: 4
            };
            map = new kakao.maps.Map(container, options);

            // 사용자 위치 마커
            var userMarker = new kakao.maps.Marker({
                position: new kakao.maps.LatLng(centerLat, centerLon),
                map: map,
                title: "내 위치"
            });

            // 시설 마커들 표시
            facilities.forEach(f => {
                var marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(f.lat, f.lon),
                    map: map,
                    title: f.name
                });

                var iwContent = `<div style="padding:5px;">${f.name}</div>`;
                var infowindow = new kakao.maps.InfoWindow({
                    content: iwContent
                });

                kakao.maps.event.addListener(marker, 'click', function() {
                    infowindow.open(map, marker);
                });
            });
        }
    </script>
</body>
</html>
