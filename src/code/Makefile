Satellite = Satellite.java
Object3D = Object3D.java
Player = Player.java $(Object3D)
Pointer = Pointer.java $(Object3D)
Building = Building.java
Planet = Planet.java $(Building)
Difficulty = Difficulty.java
Mode = Mode.java $(Difficulty)
LabelsMaster = LabelsMaster.java $(Mode)
Game = Game.java $(LabelsMaster, Planet, Player, Pointer, Satellite)
MediaHolder = MediaHolder.java $(Game)
Main = Main.java $(MediaHolder)
Manager = Manager.java Command.java File_IO.java $(Table)
UserInterface = UserInterface.java Parser.java Command.java $(Manager)


%: %.java
	javac --module-path FX/lib --add-modules=javafx.controls $($@)
	java -ea $@
