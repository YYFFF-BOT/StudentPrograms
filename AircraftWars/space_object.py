import math
import config


class SpaceObject:
    def __init__(self, x, y, width, height, angle, obj_type, id):

        self.x = x
        self.y = y
        self.id = id
        # Enter your code here
        self.width = width
        self.height = height
        self.angle = angle
        self.obj_type = obj_type
        self.radius = config.radius[self.obj_type]

    def turn_left(self):
        self.angle += config.angle_increment
        self.angle = self.angle % 360

    def turn_right(self):
        self.angle -= config.angle_increment
        self.angle = self.angle % 360

    def move_forward(self):
        x_move = float(math.cos(math.radians(self.angle)) * config.speed[self.obj_type])
        y_move = float(math.sin(math.radians(self.angle)) * config.speed[self.obj_type])

        self.x += x_move
        self.y -= y_move
        # If object out of the screen, back from the other side
        if self.x > self.width:
            self.x -= self.width
        if self.x < 0:
            self.x += self.width
        if self.y > self.height:
            self.y -= self.height
        if self.y < 0:
            self.y += self.height

    def get_xy(self):
        return self.x, self.y

    def collide_with(self, other):
        other_x = other.x
        other_y = other.y
        other_radius = config.radius[other.obj_type]
        distance_collide = self.radius + other_radius
        # Consider 4 distance, get the min of them and compare with the sum of radius
        distance = math.sqrt((self.y - other_y) ** 2 + (self.x - other_x) ** 2)
        distance_border_considered_x = math.sqrt((self.y - other_y) ** 2 + (self.width - abs(self.x - other_x)) ** 2)
        distance_border_considered_y = math.sqrt((self.height - abs(self.y - other_y)) ** 2 + (self.x - other_x) ** 2)
        distance_border_considered_xy = math.sqrt(
            (self.height - abs(self.y - other_y)) ** 2 + (self.width - abs(self.x - other_x)) ** 2)
        distance_min = min(distance, distance_border_considered_xy, distance_border_considered_x,
                           distance_border_considered_y)
        if distance_min <= distance_collide:
            return True
        else:
            return False

    def __repr__(self):
        state = "{} {},{},{},{}".format(self.obj_type, format(self.x, ".1f"), format(self.y, ".1f"), int(self.angle),
                                        self.id)
        return state
