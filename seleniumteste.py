from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
import time

# Caminho onde o geckodriver está localizado
driver_path = r"C:\Users\Rafaella\Downloads\geckodriver-v0.36.0-win-aarch64\geckodriver.exe"

# Inicializando o WebDriver do Firefox
driver = webdriver.Firefox(executable_path=driver_path)

try:
    driver.get("https://www.google.com")

    search_box = driver.find_element(By.NAME, "q")
    search_box.send_keys("Selenium WebDriver")
    search_box.send_keys(Keys.RETURN)

    time.sleep(2)

    results = driver.find_elements(By.XPATH, "//div[@class='g']")
    print(f"Resultados encontrados: {len(results)}")
    assert len(results) > 0, "Nenhum resultado encontrado!"

    print("Teste concluído com sucesso!")

finally:
    driver.quit()
