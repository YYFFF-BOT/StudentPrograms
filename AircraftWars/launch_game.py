"""
This is the entry point to your game.

Launch the game by running `python3 launch_game.py`
"""

from game_engine import Engine
from gui import GUI
from player import Player

game = Engine('examples/txt', Player, GUI)

game.run_game()
