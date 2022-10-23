
import config
import math


class Player:
    def __init__(self):
        pass

    def action(self, spaceship, asteroid_ls, bullet_ls, fuel, score):
        thrust = False
        left = False
        right = False
        shoot = False
        # Get the target
        spaceship_x = spaceship.x
        spaceship_y = spaceship.y
        distance_ls = []
        height = spaceship.height
        width = spaceship.width
        # Get the closet asteroid
        for asteroid in asteroid_ls:
            asteroid_x = asteroid.x
            asteroid_y = asteroid.y
            distance = math.sqrt((spaceship_y - asteroid_y) ** 2 + (spaceship_x - asteroid_x) ** 2)
            distance_ls.append(distance)
        distance_min = min(distance_ls)
        target = asteroid_ls[distance_ls.index(distance_min)]
        time_track = (distance_min - config.speed["bullet"] * config.bullet_move_count) / (
                config.speed["spaceship"] - config.speed[target.obj_type])

        # If the asteroid is going to fade, find the other target
        if len(asteroid_ls) > 1:
            if target.angle == 0:
                time_fade = (width - target.x) / config.speed[target.obj_type]
            elif target.angle == 90:
                time_fade = target.y / config.speed[target.obj_type]
            elif target.angle == 180:
                time_fade = target.x / config.speed[target.obj_type]
            elif target.angle == 270:
                time_fade = (height - target.y) / config.speed[target.obj_type]
            else:
                time_fade = min(
                    (width - target.x) / (math.cos(math.radians(target.angle)) * config.speed[target.obj_type]),
                    target.y / (math.sin(math.radians(target.angle)) * config.speed[target.obj_type]),
                    target.x / (math.cos(math.radians(target.angle)) * config.speed[target.obj_type]),
                    (height - target.y) / (math.sin(math.radians(target.angle)) * config.speed[target.obj_type]))

            if abs(time_fade) <= time_track:
                distance_ls.remove(distance_min)
                distance_min = min(distance_ls)
                target = asteroid_ls[distance_ls.index(distance_min)]

        # Calculate the angle
        asteroid_x = target.x
        asteroid_y = target.y

        angle = 0
        if asteroid_x == spaceship_x:
            if spaceship_y < asteroid_y:
                angle = 270
            if spaceship_y > asteroid_y:
                angle = 90

        elif asteroid_y == spaceship_y:
            if spaceship_x > asteroid_x:
                angle = 180
            if spaceship_x < asteroid_x:
                angle = 0
        else:
            angle = math.atan((spaceship_y - asteroid_y) / (spaceship_x - asteroid_x)) * 180 / math.pi

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

        # Aim the target
        angle_diff = abs(spaceship.angle - angle)

        # Do not turn if has aimed accurately
        if angle_diff < config.angle_increment / 3 *2 or 360 - angle_diff < config.angle_increment / 3 *2:
            right = False
            left = False

        elif angle > spaceship.angle and angle_diff <= 180:
            left = True

        elif spaceship.angle > angle and 360-spaceship.angle + angle < 180:
            left = True

        elif spaceship.angle > angle and angle_diff < 180:
            right = True

        elif angle > spaceship.angle and 360 - angle + spaceship.angle < 180:
            right = True

        # Thrust when the spaceship is not back at the target
        if angle_diff < config.angle_increment * 4 or 360 - angle_diff < config.angle_increment *4:
            thrust = True
            # If has aimed accurately
            if angle_diff <= config.angle_increment/2 or 360 - angle_diff <= config.angle_increment / 2:
                # If the distance is close enough and no bullet has shoot, shoot the bullet
                if distance_min < config.bullet_move_count * (config.speed["bullet"] - config.speed[target.obj_type]) and len(bullet_ls) < 2:
                    shoot = True

        return (thrust, left, right, shoot)