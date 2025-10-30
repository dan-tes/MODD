import json
import matplotlib.pyplot as plt

def main():
    with open("data.json", "r", encoding="utf-8") as f:
        data = json.load(f)

    graphs = data["graphs"]

    plt.figure(figsize=(7, 5))
    for g in graphs:
        plt.plot(g["x"], g["y"], marker='o', label=g.get("label", "Без названия"))

    plt.title("Статистика симуляции")
    plt.xlabel("Время")
    plt.ylabel("Значения")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()

    plt.savefig("chart.png")
    plt.close()

if __name__ == "__main__":
    main()
