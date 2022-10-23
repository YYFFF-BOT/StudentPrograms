game_name = "INFO1110-Cosmic-Warrior"
frame_delay = 0.05  # seconds between each frame (0.03 =~ 33 fps)

radius = {"spaceship": 12,
          "bullet": 3,
          "asteroid_small": 16,
          "asteroid_large": 32}

angle_increment = 15

speed = {"spaceship": 10,
         "bullet": 30,
         "asteroid_small": 3,
         "asteroid_large": 3}
# 子弹飞行多少帧
bullet_move_count = 5

shoot_small_ast_score = 150
shoot_large_ast_score = 100
collide_score = -100

# 燃料小于多少不能射击
shoot_fuel_threshold = 10
# 燃料小于%多少时候警告
fuel_warning_threshold = ()
# 每一帧消耗的燃料
spaceship_fuel_consumption = 1
# 射击一次消耗的燃料
bullet_fuel_consumption = 2
