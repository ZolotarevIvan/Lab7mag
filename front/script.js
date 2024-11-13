
function calculate() {
    const num1 = parseFloat(document.getElementById("num1").value);
    const num2 = parseFloat(document.getElementById("num2").value);
    const operation = document.querySelector('input[name="operation"]:checked').value;
    let result;

    // Выполняем операцию на основе выбранной радиокнопки
    switch (operation) {
        case "add":
            result = num1 + num2;
            break;
        case "subtract":
            result = num1 - num2;
            break;
        case "multiply":
            result = num1 * num2;
            break;
        case "divide":
            result = num2 !== 0 ? num1 / num2 : "Ошибка: деление на ноль";
            break;
        default:
            result = "Неверная операция";
    }

    // Сохраняем значения в localStorage для передачи на результирующую страницу
    localStorage.setItem("num1", num1);
    localStorage.setItem("num2", num2);
    localStorage.setItem("operation", operation);
    localStorage.setItem("result", result);

    // Переходим на страницу результата
    window.location.href = "result.html";
}