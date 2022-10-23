"""
score: 6400
一直加速

import config
import math

class Player:
    def __init__(self):
        pass

    def action(self, spaceship, asteroid_ls, bullet_ls, fuel, score):
        thrust = True
        left = False
        right = False
        shoot = False

        spaceship_x = spaceship.x
        spaceship_y = spaceship.y
        distance_ls = []
        for asteroid in asteroid_ls:
            asteroid_x = asteroid.x
            asteroid_y = asteroid.y
            distance = math.sqrt((spaceship_y-asteroid_y)**2+(spaceship_x-asteroid_x)**2)
            distance_ls.append(distance)
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
                angle += 90

            # Asteroid at third quadrant
            if asteroid_x < spaceship_x and asteroid_y > spaceship_y:
                angle = 180 - angle

            # Asteroid at fourth quadrant
            if asteroid_x > spaceship_x and asteroid_y > spaceship_y:
                angle = 360 - angle

        # Aim the target
        angle_diff = spaceship.angle - angle
        if -config.angle_increment < angle_diff < config.angle_increment:
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(bullet_ls) < 3:
                shoot = True
        elif angle_diff > 0:
            # Spaceship Turnright
            right = True

        elif angle_diff < 0:
            # Spaceship Turnleft
            left = True

        return (thrust, left, right, shoot)

"""

""" 
score: 7150
转向后再加速

import config
import math
class Player:
    def __init__(self):

        # Enter your code here

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
        for asteroid in asteroid_ls:
            asteroid_x = asteroid.x
            asteroid_y = asteroid.y
            distance = math.sqrt((spaceship_y - asteroid_y) ** 2 + (spaceship_x - asteroid_x) ** 2)
            distance_ls.append(distance)
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
        angle_diff = spaceship.angle - angle

        if abs(angle_diff) < 90 or distance_min / config.speed[target.obj_type] < angle_diff / config.angle_increment:
            thrust = True

        if angle_diff > 0:
            # Spaceship Turnright
            right = True

        if angle_diff < 0:
            # Spaceship Turnleft
            left = True

        if -config.angle_increment / 2 < angle_diff < config.angle_increment / 2:
            # Shoot when distance is close enough

            right = False
            left = False
        if -config.angle_increment < angle_diff < config.angle_increment:
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(bullet_ls) < 2:
                shoot = True

        return thrust, left, right, shoot"""

"""
score: 7900

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
        time_track = (distance_min-config.speed["bullet"]*config.bullet_move_count) / (config.speed["spaceship"] - config.speed[target.obj_type])

        # If the asteroid is going to fade, find the other target
        if len(asteroid_ls) > 1:
            if target.angle == 0:
                time_fade = (width - target.x)/config.speed[target.obj_type]
            elif target.angle == 90:
                time_fade = target.y/config.speed[target.obj_type]
            elif target.angle == 180:
                time_fade = target.x/config.speed[target.obj_type]
            elif target.angle == 270:
                time_fade = (height - target.y)/config.speed[target.obj_type]
            else:
                time_fade = min((width-target.x)/(math.cos(math.radians(target.angle)) * config.speed[target.obj_type]),
                                target.y/(math.sin(math.radians(target.angle))*config.speed[target.obj_type]),
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
        angle_diff = spaceship.angle - angle

        # If spaceship is not back at the target
        if abs(angle_diff) < config.angle_increment * 4 or distance_min / config.speed[target.obj_type] < angle_diff / config.angle_increment:
            thrust = True

        if angle_diff > 0:
            right = True

        if angle_diff < 0:
            left = True

        # If aim accurately
        if -config.angle_increment / 2 < angle_diff < config.angle_increment / 2:
            right = False
            left = False

        if -config.angle_increment < angle_diff < config.angle_increment:
            # Shoot when distance is close enough
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(bullet_ls) < 2:
                shoot = True

        return thrust, left, right, shoot

"""

"""
score: 8300

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
        angle_diff = spaceship.angle - angle

        if angle_diff < config.angle_increment * 4 or distance_min / config.speed[target.obj_type] < angle_diff / config.angle_increment:
            thrust = True
        if -config.angle_increment / 2 < angle_diff < config.angle_increment / 2:
            right = False
            left = False
        elif angle > spaceship.angle+180 or angle < spaceship.angle:
            right = True

        elif angle < spaceship.angle+180:
            left = True
        if -config.angle_increment < angle_diff < config.angle_increment:
            # Shoot when distance is close enough
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(bullet_ls) < 2:
                shoot = True

        return thrust, left, right, shoot
"""

"""
score 8950

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

        if angle_diff < config.angle_increment / 2:
            right = False
            left = False

        elif angle > spaceship.angle and angle_diff <= 180:
            left = True

        elif spaceship.angle > angle and 360-spaceship.angle +angle < 180:
            left = True

        elif spaceship.angle > angle and angle_diff < 180:
            right = True

        elif angle > spaceship.angle and 360 - angle + spaceship.angle < 180:
            right = True

        if angle_diff < config.angle_increment * 4:
            thrust = True
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(bullet_ls) < 2 and angle_diff < config.angle_increment/2:
                shoot = True

        return thrust, left, right, shoot
"""

"""
score 8750

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

        if angle_diff < config.angle_increment / 2 or (360 - angle_diff) < config.angle_increment:
            right = False
            left = False

        elif angle > spaceship.angle and angle_diff <= 180:
            left = True

        elif spaceship.angle > angle and 360 - spaceship.angle + angle < 180:
            left = True

        elif spaceship.angle > angle and angle_diff < 180:
            right = True

        elif angle > spaceship.angle and 360 - angle + spaceship.angle < 180:
            right = True

        if angle_diff < config.angle_increment * 4 or (360 - angle_diff) < config.angle_increment * 4:
            thrust = True
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(
                    bullet_ls) < 2 and angle_diff < config.angle_increment / 2:
                shoot = True

        return thrust, left, right, shoot
"""

"""
score 8450

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

        if angle_diff < config.angle_increment / 2 or (360 - angle_diff) < config.angle_increment:
            right = False
            left = False

        elif angle > spaceship.angle and angle_diff <= 180:
            left = True

        elif spaceship.angle > angle and 360 - spaceship.angle + angle < 180:
            left = True

        elif spaceship.angle > angle and angle_diff < 180:
            right = True

        elif angle > spaceship.angle and 360 - angle + spaceship.angle < 180:
            right = True

        if angle_diff < config.angle_increment * 4:
            thrust = True
            if distance_min < config.bullet_move_count * config.speed["bullet"] and len(
                    bullet_ls) < 2 and angle_diff < config.angle_increment / 2:
                shoot = True

        return thrust, left, right, shoot

"""

"""
score 9000

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

        if angle_diff < config.angle_increment / 3 *2 or 360 - angle_diff < config.angle_increment / 3 *2:
            right = False
            left = False

        elif angle > spaceship.angle and angle_diff <= 180:
            left = True

        elif spaceship.angle > angle and 360-spaceship.angle +angle < 180:
            left = True

        elif spaceship.angle > angle and angle_diff < 180:
            right = True

        elif angle > spaceship.angle and 360 - angle + spaceship.angle < 180:
            right = True

        if angle_diff < config.angle_increment * 4 or 360 - angle_diff < config.angle_increment *4:
            thrust = True
            if angle_diff < config.angle_increment/2 or 360 - angle_diff < config.angle_increment / 2:
                if distance_min < config.bullet_move_count * (config.speed["bullet"] - config.speed[target.obj_type]) and len(bullet_ls) < 2:
                    shoot = True

        return thrust, left, right, shoot"""