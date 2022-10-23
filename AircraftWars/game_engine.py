import config
from space_object import SpaceObject


class Engine:
    def __init__(self, game_state_filename, player_class, gui_class):
        data_ls = self.import_state(game_state_filename)
        self.width = data_ls[0]
        self.height = data_ls[1]
        self.score = data_ls[2]
        self.fuel = data_ls[3]
        self.spaceship_info = data_ls[4]
        self.bullets_count = data_ls[5]
        self.asteroids_count = data_ls[6]
        self.asteroid_ls = data_ls[7]
        self.upcoming_asteroids_count = data_ls[8]
        self.upcoming_asteroid_ls = data_ls[9]
        self.bullet_ls = data_ls[10]
        self.asteroid_info_ls = data_ls[11]
        self.upcoming_asteroids_info_ls = data_ls[12]

        self.player = player_class()
        self.spaceship = SpaceObject(float(self.spaceship_info[0]), float(self.spaceship_info[1]), self.width,
                                     self.height,
                                     float(self.spaceship_info[2]),
                                     "spaceship", int(self.spaceship_info[3]))

        self.GUI = gui_class(self.width, self.height)

    def import_state(self, game_state_filename):

        try:
            with open(game_state_filename, "r") as f:
                file_info = f.readlines()
                if len(file_info) < 8:
                    raise ValueError("Error: game state incomplete")
                width = 0
                asteroids_count = 0
                upcoming_asteroids_count = 0
                asteroid_info_ls = []
                upcoming_asteroids_info_ls = []
                bullet_ls = []
                i = 0
                while i < len(file_info):
                    data_line = file_info[i]
                    data_line = data_line.split(" ")
                    # Examine whether first data is width
                    if i == 0:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "width":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        width = int(data_line[1])
                    # Examine whether second data is height
                    elif i == 1:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "height":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        height = int(data_line[1])

                    # Examine whether third data is score
                    elif i == 2:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "score":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        score = int(data_line[1])

                    # Examine whether fourth data is spaceship
                    elif i == 3:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n").split(",")
                        if data_line[0] != "spaceship":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if len(data_line[1]) != 4:
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        spaceship_info = data_line[1]

                    # Examine whether fifth data is fuel
                    elif i == 4:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "fuel":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        fuel = int(data_line[1])

                    # Examine whether sixth data is asteroids_count
                    elif i == 5:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "asteroids_count":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        if int(data_line[1]) < 0:
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        asteroids_count = int(data_line[1])

                    # Examine whether from sixth data to six + asteroids_count data is asteroid info
                    elif 5 < i < 6 + asteroids_count:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))

                        data_line[1] = data_line[1].strip("\n").split(",")

                        if data_line[0] != "asteroid_large" and data_line[0] != "asteroid_small":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if len(data_line[1]) != 4:
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        for j in data_line[1]:
                            data = j.split(".")
                            for g in data:
                                if not g.isdigit():
                                    raise ValueError("Error: invalid data type in line {}".format(i + 1))
                            if j == "":
                                raise ValueError("Error: game state incomplete")
                        if data_line[0] == "asteroid_large":
                            data_line[1].append("asteroid_large")
                            asteroid_info_ls.append(data_line[1])
                        elif data_line[0] == "asteroid_small":
                            data_line[1].append("asteroid_small")
                            asteroid_info_ls.append(data_line[1])

                    # Examine the data bullets_count
                    elif i == 6 + asteroids_count:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "bullets_count":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        bullets_count = int(data_line[1])

                    # Examine the data upcoming_asteroids_count
                    elif i == 7 + asteroids_count:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n")
                        if data_line[0] != "upcoming_asteroids_count":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if not data_line[1].isdigit():
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        if int(data_line[1]) < 0:
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        upcoming_asteroids_count = int(data_line[1])

                    # Examine the data upcoming_asteroids and store the data in list
                    elif i > 7 + asteroids_count:
                        if len(data_line) != 2:
                            raise ValueError("Error: expecting a key and value in line {}".format(i + 1))
                        data_line[1] = data_line[1].strip("\n").split(",")
                        if data_line[0] != "upcoming_asteroid_large" and data_line[0] != "upcoming_asteroid_small":
                            raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0], i + 1))
                        if len(data_line[1]) != 4:
                            raise ValueError("Error: invalid data type in line {}".format(i + 1))
                        for j in data_line[1]:
                            data = j.split(".")
                            for g in data:
                                if not g.isdigit():
                                    raise ValueError("Error: invalid data type in line {}".format(i + 1))
                            if j == "":
                                raise ValueError("Error: game state incomplete")
                        if data_line[0] == "upcoming_asteroid_large":
                            data_line[1].append("asteroid_large")
                            upcoming_asteroids_info_ls.append(data_line[1])
                        elif data_line[0] == "upcoming_asteroid_small":
                            data_line[1].append("asteroid_small")
                            upcoming_asteroids_info_ls.append(data_line[1])

                    i += 1

                # Examine whether the number of upcoming asteroids is equal to the upcoming asteroids count
                if 8 + upcoming_asteroids_count + asteroids_count > len(file_info):
                    raise ValueError("Error: expecting a key and value in line {}".format(
                        8 + upcoming_asteroids_count + asteroids_count))
                elif 8 + upcoming_asteroids_count + asteroids_count < len(file_info):
                    data_line = file_info[8 + upcoming_asteroids_count + asteroids_count].split(" ")
                    raise ValueError("Error: unexpected key: {} in line {}".format(data_line[0],
                                                                                   8 + upcoming_asteroids_count + asteroids_count))
        except FileNotFoundError:
            print("Error: unable to open {}".format(game_state_filename))
        # Store the data of asteroids as the objects
        count = 0
        asteroid_ls = []
        while count < asteroids_count:
            asteroid = SpaceObject(float(asteroid_info_ls[count][0]),
                                   float(asteroid_info_ls[count][1]), width, height,
                                   float(asteroid_info_ls[count][2]), asteroid_info_ls[count][4],
                                   int(asteroid_info_ls[count][3]))
            asteroid_ls.append(asteroid)
            count += 1

        # Store the data of upcoming_asteroids as the objects
        count = 0
        upcoming_asteroid_ls = []

        while count < upcoming_asteroids_count:
            upcoming_asteroid = SpaceObject(float(upcoming_asteroids_info_ls[count][0]),
                                            float(upcoming_asteroids_info_ls[count][1]), width, height,
                                            float(upcoming_asteroids_info_ls[count][2]),
                                            upcoming_asteroids_info_ls[count][4],
                                            int(upcoming_asteroids_info_ls[count][3]))
            upcoming_asteroid_ls.append(upcoming_asteroid)
            count += 1
        return width, height, score, fuel, spaceship_info, bullets_count, asteroids_count, asteroid_ls, upcoming_asteroids_count, upcoming_asteroid_ls, bullet_ls, asteroid_info_ls, upcoming_asteroids_info_ls

    def export_state(self, game_state_filename):
        try:
            with open(game_state_filename, "w") as f:
                f.write("width {}\n".format(self.width))
                f.write("height {}\n".format(self.height))
                f.write("score {}\n".format(self.score))
                f.write(
                    "spaceship {},{},{},{}\n".format(format(self.spaceship.x, ".1f"), format(self.spaceship.y, ".1f"),
                                                     int(self.spaceship.angle),
                                                     self.spaceship.id))
                f.write("fuel {}\n".format(self.fuel))
                f.write("asteroids_count {}\n".format(self.asteroids_count))
                i = 0
                while i < self.asteroids_count:
                    f.write(
                        "{} {},{},{},{}\n".format(self.asteroid_ls[i].obj_type,
                                                  format(self.asteroid_ls[i].x, ".1f"),
                                                  format(self.asteroid_ls[i].y, ".1f"),
                                                  int(self.asteroid_ls[i].angle),
                                                  self.asteroid_ls[i].id))
                    i += 1
                f.write("bullets_count {}\n".format(self.bullets_count))
                i = 0

                while i < self.bullets_count:
                    f.write(
                        "{} {},{},{},{}\n".format(self.bullet_ls[i].obj_type,
                                                  format(self.bullet_ls[i].x, ".1f"),
                                                  format(self.bullet_ls[i].y, ".1f"),
                                                  int(self.bullet_ls[i].angle),
                                                  self.bullet_ls[i].id))
                    i += 1
                f.write("upcoming_asteroids_count {}\n".format(self.upcoming_asteroids_count))
                i = 0
                while i < self.upcoming_asteroids_count:
                    f.write(
                        "{} {},{},{},{}\n".format("upcoming_" + self.upcoming_asteroid_ls[i].obj_type,
                                                  format(self.upcoming_asteroid_ls[i].x, ".1f"),
                                                  format(self.upcoming_asteroid_ls[i].y, ".1f"),
                                                  int(self.upcoming_asteroid_ls[i].angle),
                                                  self.upcoming_asteroid_ls[i].id))
                    i += 1
        except FileNotFoundError:
            print("Error: unable to open {}".format(game_state_filename))

    def run_game(self):

        frame_count = 1
        initial_fuel = self.fuel
        bullet_id = 0
        bullet_start_frame = []
        game_continue = True
        warning_threshold = []
        warning = True

        if type(config.fuel_warning_threshold) != int:
            warning_threshold = list(config.fuel_warning_threshold)

        while game_continue:
            shoot = False
            # Get the operation from player
            play_state = self.player.action(self.spaceship, self.asteroid_ls, self.bullet_ls, self.fuel, self.score)

            if play_state[1]:
                self.spaceship.turn_left()
            if play_state[2]:
                self.spaceship.turn_right()
            if play_state[3]:
                shoot = True
            if play_state[0]:
                self.spaceship.move_forward()

            # Update positions of asteroids
            for i in self.asteroid_ls:
                i.move_forward()

            # If player shoot, generate the bullets
            if shoot:
                # Do not shoot due to low fuel
                if self.fuel < config.shoot_fuel_threshold:
                    print("Cannot shoot due to low fuel")
                else:
                    self.fuel -= config.bullet_fuel_consumption
                    bullet = SpaceObject(self.spaceship.x, self.spaceship.y, self.width, self.height,
                                         self.spaceship.angle, "bullet",
                                         bullet_id)
                    bullet_start_frame.append(frame_count)
                    self.bullet_ls.append(bullet)
                    self.bullets_count += 1
                    bullet_id += 1

            # Remove expired bullets
            if len(bullet_start_frame) >= 1 and len(self.bullet_ls) >= 1:
                if frame_count - bullet_start_frame[0] == config.bullet_move_count:
                    del bullet_start_frame[0]
                    del self.bullet_ls[0]
                    self.bullets_count -= 1

            # Update positions of bullets
            for i in self.bullet_ls:
                i.move_forward()

            # Minus the fuel consumption for each frame
            self.fuel -= config.spaceship_fuel_consumption

            # Print the warning message
            # Only need to warn 1 time
            if type(config.fuel_warning_threshold) == int:
                if self.fuel / initial_fuel <= config.fuel_warning_threshold/100 and warning:
                    print("{}% fuel warning: {} remaining".format(config.fuel_warning_threshold, self.fuel))
                    warning = False
            # Need to warn more than 1 times
            elif len(warning_threshold) >= 1:
                if self.fuel / initial_fuel <= warning_threshold[0]/100:
                    print("{}% fuel warning: {} remaining".format(warning_threshold[0], self.fuel))
                    del warning_threshold[0]

            # Detect collisions between asteroids and bullets
            for i in self.asteroid_ls:
                for j in self.bullet_ls:
                    if j.collide_with(i):
                        if i.obj_type == "asteroid_small":
                            self.score += config.shoot_small_ast_score
                        elif i.obj_type == "asteroid_large":
                            self.score += config.shoot_large_ast_score
                        print("Score: {} \t [Bullet {} has shot asteroid {}]".format(self.score, j.id, i.id))
                        del bullet_start_frame[self.bullet_ls.index(j)]
                        self.asteroid_ls.remove(i)
                        self.bullet_ls.remove(j)
                        self.bullets_count -= 1
                        # Add new asteroid to screen
                        if self.upcoming_asteroids_count != 0:
                            print("Added asteroid {}".format(self.upcoming_asteroid_ls[0].id))
                            self.asteroid_ls.append(self.upcoming_asteroid_ls[0])
                            self.upcoming_asteroids_count -= 1
                            del self.upcoming_asteroid_ls[0]
                        # End the game if there is no asteroid available
                        else:
                            self.asteroids_count -= 1
                            print("Error: no more asteroids available")
                            self.GUI.finish(self.score)
                            game_continue = False
                            break

            # Detect collisions between asteroids and spaceship
            for i in self.asteroid_ls:
                if self.spaceship.collide_with(i):
                    self.score += config.collide_score
                    print("Score: {} \t [Spaceship collided with asteroid {}]".format(self.score, i.id))
                    self.asteroid_ls.remove(i)
                    # Add new asteroid to screen
                    if self.upcoming_asteroids_count != 0:
                        self.asteroid_ls.append(self.upcoming_asteroid_ls[0])
                        print("Added asteroid {}".format(self.upcoming_asteroid_ls[0].id))
                        self.upcoming_asteroids_count -= 1
                        del self.upcoming_asteroid_ls[0]

                    # End the game if there is no asteroid available
                    else:
                        self.asteroids_count -= 1
                        print("Error: no more asteroids available")
                        self.GUI.finish(self.score)
                        game_continue = False
                        break
            # If fuel runs out, end the game
            if self.fuel <= 0:
                self.GUI.finish(self.score)
                game_continue = False
            # Draw the game state
            self.GUI.update_frame(self.spaceship, self.asteroid_ls, self.bullet_ls, self.score, self.fuel)
            frame_count += 1

