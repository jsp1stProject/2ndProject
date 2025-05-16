<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>추천 사료 결과</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f9fafb;
      padding: 30px;
    }
    .result {
      max-width: 800px;
      margin: auto;
    }
    h2 {
      text-align: center;
    }
    .calorie {
      font-size: 20px;
      margin-bottom: 20px;
      text-align: center;
      color: #374151;
    }
    .food-list {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;
    }
    .food-item {
      background: #fff;
      padding: 15px;
      border-radius: 12px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.05);
    }
    .food-item img {
      max-width: 100%;
      height: auto;
      border-radius: 8px;
    }
    .food-item h3 {
      font-size: 18px;
      margin: 10px 0 5px;
    }
    .food-item p {
      font-size: 14px;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="result">
    <h2>🐾 추천 사료 결과 🐾</h2>
    <div class="calorie">
      <p>계산된 하루 권장 칼로리: <strong>${der} kcal</strong></p>
      <p><a href="calorieCalculator">다시 계산하기</a></p>
    </div>

    <div class="food-list">
      <c:forEach var="food" items="${foodList}">
        <div class="food-item">
          <img src="${food.image_url}" alt="${food.title}">
          <h3>${food.title}</h3>
          <p>가격: ${food.price}</p>
          <p>원산지: ${food.origin}</p>
          <p>권장 연령: ${food.age_recommendation}</p>
          <p><a href="${food.detail_url}" target="_blank">상세보기</a></p>
        </div>
      </c:forEach>
    </div>
  </div>
</body>
</html>
