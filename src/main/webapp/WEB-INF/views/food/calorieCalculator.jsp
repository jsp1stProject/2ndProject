<%@ page contentType="text/html;charset=UTF-8" %>
<style>
  /** {*/
  /*  box-sizing: border-box;*/
  /*}*/

  /*html, body {*/
  /*  margin: 0;*/
  /*  padding: 0;*/
  /*  font-family: 'Segoe UI', sans-serif;*/
  /*  background-color: #f4f6f8;*/
  /*  height: 100%;*/
  /*}*/

  .wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: calc(100vh - 60px);
  }

  .calculator {
    background-color: #ffffff;
    padding: 30px 40px;
    border-radius: 16px;
    box-shadow: 0 0 28px rgba(0, 0, 0, 0.15);
    width: 100%;
    max-width: 440px;
    text-align: center;
  }

  h2 {
    margin-bottom: 25px;
    color: #333;
    word-break: keep-all;
  }

  form {
    display: flex;
    flex-direction: column;
  }

  label {
    margin: 12px 0 4px;
    text-align: left;
    font-weight: bold;
    color: #444;
  }

  input, select {
    padding: 8px 10px;
    border: 1px solid #ccc;
    border-radius: 6px;
    font-size: 14px;
  }

</style>
<div class="container pt-header position-relative">
  <div class="wrapper">
    <div class="calculator">
      <h2>🐾 반려동물 권장 칼로리 계산</h2>
      <form method="post" action="${pageContext.request.contextPath}/food/recommend">
        <label for="animal">동물 종류:</label>
        <select id="animal" name="animal" onchange="updateActivityOptions()">
          <option value="dog">강아지 🐶</option>
          <option value="cat">고양이 🐱</option>
        </select>

        <label for="weight">체중 (kg):</label>
        <input type="number" id="weight" name="weight" placeholder="예: 5" step="0.1" required>

        <label for="activity">활동 지수 선택:</label>
        <select id="activity" name="activity"></select>

        <button class="btn btn-primary mt-4" type="submit">계산하기</button>
      </form>
    </div>
  </div>

  <script>
    const activityOptions = {
      dog: [
        { value: 1.0, label: "1.0 - 체중 감량 중" },
        { value: 1.2, label: "1.2 - 노령견 (8세이상)" },
        { value: 1.6, label: "1.6 - 중성화된 성견" },
        { value: 1.8, label: "1.8 - 미중성화 성견" },
        { value: 2.0, label: "2.0 - 체중 증가/활동적" },
        { value: 2.5, label: "2.5 - 강아지 (성장기)" },
        { value: 3.0, label: "3.0 - 강아지 (0~4개월)" }
      ],
      cat: [
        { value: 0.8, label: "0.8 - 체중 감량 중" },
        { value: 1.0, label: "1.0 - 노령묘 (8세이상)" },
        { value: 1.2, label: "1.2 - 중성화된 성묘" },
        { value: 1.4, label: "1.4 - 미중성화 성묘" },
        { value: 1.5, label: "1.5 - 체중 증가 중" },
        { value: 2.0, label: "2.0 - 성장기 고양이 (4~12개월)" },
        { value: 2.5, label: "2.5 - 새끼 고양이 (0~4개월)" }
      ]
    };

    function updateActivityOptions() {
      const animal = document.getElementById('animal').value;
      const activitySelect = document.getElementById('activity');
      activitySelect.innerHTML = "";
      activityOptions[animal].forEach(option => {
        const opt = document.createElement("option");
        opt.value = option.value;
        opt.textContent = option.label;
        activitySelect.appendChild(opt);
      });
    }

    // 초기 실행
    updateActivityOptions();
  </script>

</div>
