package world.components;

/* CONE, CUBE, DIAMOND, BALL, KEY, TICKET and TORCH should remain between
 * OUTOFBOUNDS and CHEST
 **/

public enum CellType {
	EMPTY			// 0
	, WALL			// 1
	, DOOR			// 2
	, TELEPORT		// 3
	, KEYDOOR		// 4
	, OUTOFBOUNDS	// 5
	, CONE			// 6
	, CUBE			// 7
	, DIAMOND		// 8
	, BALL			// 9
	, KEY			// 10
	, TICKET		// 11
	, TORCH			// 12
	, CHEST			// 13
	, RINGS			// 14
	, BRIEFCASE		// 15
	, DRAWERS		// 16
	, TABLE			// 17
	, BED			// 18
	, COUCH			// 19
	, PLAYER;		// 20
}