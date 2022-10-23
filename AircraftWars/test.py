import math
"""
spaceship 392.3,403.2,285,0 asteroid_small 502.9,406.2,356,8
-73.45671052234576 285.0 358.45671052234576

spaceship 397.3,411.9,300,0 asteroid_small 505.8,406.4,356,8
297.1127512251733 300.0 2.8872487748266598
"""
spaceship_x = 397.3
spaceship_y = 400.9
asteroid_x = 505.8
asteroid_y = 406.4

angle = math.atan((spaceship_y - asteroid_y) / (spaceship_x - asteroid_x)) * 180 / math.pi
print(angle)
# Asteroid at first quadrant
if asteroid_x > spaceship_x and asteroid_y < spaceship_y:
    angle = abs(angle)

# Asteroid at second quadrant
if asteroid_x < spaceship_x and asteroid_y < spaceship_y:
    angle = 180 - angle

# Asteroid at third quadrant
if asteroid_x < spaceship_x and asteroid_y > spaceship_y:
    angle = 180 - angle

# Asteroid at fourth quadrant
if asteroid_x > spaceship_x and asteroid_y > spaceship_y:
    angle = 360 - angle
print(angle)








"""
尝试使用动态变量赋值 失败 可以输出无法使用
data_line = file_info[i]
            data_line = data_line.split(" ")

            if i < 3 or 3 < i < 6:
                if len(data_line) != 2:
                    raise ValueError("Error: expecting a key and value in line {}".format(i+1))
                
                data_line[1] = data_line[1].strip("\n")

                if data_line[0] != key_ls[i]:
                    raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i+1))
                if not data_line[1].isdigit():
                    raise ValueError("Error: invalid data type in line {}".format(i+1))
                names[key_ls[i]] = int(data_line[1])

            if i == 3:
                if len(data_line) != 2:
                    raise ValueError("Error: expecting a key and value in line {}".format(i+1))
                
                data_line[1] = data_line[1].strip("\n").split(",")

                print(data_line)
                if data_line[0] != key_ls[i]:
                    raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i+1))
                if len(data_line[1]) != 4:
                    raise ValueError("Error: invalid data type in line {}".format(i+1))
                spaceship_info = data_line[1]
"""
