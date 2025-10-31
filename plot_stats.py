import json
import numpy as np
import matplotlib.pyplot as plt

def gen_g_speed(graphs):
    plt.figure(figsize=(7, 5))
    for g in graphs:
        plt.plot(g["x"], g["y"], marker='o', label=g.get("label", "Без названия"))

    plt.title("Статистика симуляции")
    plt.xlabel("Время")
    plt.ylabel("Значения скорости")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig("chart.png")
    plt.close()

def gen_g_distribution(velocities):
    k = 1.38e-23       # постоянная Больцмана, Дж/К
    NA = 6.022e23      # число Авогадро, 1/моль

    for idx, v_info in enumerate(velocities):
        v_data = np.array(v_info["x"])   # скорости из модели
        T = v_info["T"]                  # температура из модели
        M = v_info["M"]                # молярная масса из модели

        if len(v_data) == 0:
            continue  # пропускаем, если данных нет

        # --- Теоретическая функция Максвелла ---
        v = np.linspace(0, max(v_data), 400)
        f = 4 * np.pi * (M / (2 * np.pi * NA * k * T))**1.5 * v**2 * np.exp(-M * v**2 / (2 * NA * k * T))
        f = f / np.trapz(f, v)  # нормировка

        # --- Гистограмма модельных скоростей ---
        plt.figure(figsize=(8,5))
        plt.hist(v_data, bins=40, density=True, alpha=0.5, color='skyblue', label='Модельные данные')
        plt.plot(v, f, 'r-', linewidth=2, label='Теория Максвелла')

        # --- Наиболее вероятная скорость ---
        v_p = np.sqrt(2 * NA * k * T / M)
        plt.axvline(v_p, color='orange', linestyle='--', label=f'vₚ = {v_p:.0f} м/с')

        plt.title(f'Газ {idx+1}: сравнение скоростей с распределением Максвелла')
        plt.xlabel('Скорость v, м/с')
        plt.ylabel('Плотность вероятности')
        plt.legend()
        plt.grid(True)
        plt.tight_layout()
        plt.savefig(f"chart{idx}.png")
        plt.close()

if __name__ == "__main__":
    with open("data.json", "r", encoding="utf-8") as f:
        data = json.load(f)
        graphs = data["graphs"]
        velocities = data["velocities"]

        gen_g_speed(graphs)
        gen_g_distribution(velocities)
