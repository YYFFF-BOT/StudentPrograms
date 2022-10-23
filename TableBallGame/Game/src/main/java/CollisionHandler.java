import javafx.geometry.Point2D;

public class CollisionHandler {
    public static void handleCollision(GameItem itemA, GameItem itemB) {

        //Properties of two colliding balls
        Point2D posA = new Point2D(itemA.getxPos(), itemA.getyPos());
        Point2D posB = new Point2D(itemB.getxPos(), itemB.getyPos());
        Point2D velA = new Point2D(itemA.getxVel(), itemA.getyVel());
        Point2D velB = new Point2D(itemB.getxVel(), itemB.getyVel());

        //calculate the axis of collision
        Point2D collisionVector = posB.subtract(posA);
        collisionVector = collisionVector.normalize();

        //the proportion of each balls velocity along the axis of collision
        double vA = collisionVector.dotProduct(velA);
        double vB = collisionVector.dotProduct(velB);

        //if balls are moving away from each other do nothing
        if (vA <= 0 && vB >= 0) {
            return;
        }

        // We're working with equal mass balls today
        //double mR = massB/massA;
        double mR = 1;

        //The velocity of each ball after a collision can be found by solving the quadratic equation
        //given by equating momentum energy and energy before and after the collision and finding the
        //velocities that satisfy this
        //-(mR+1)x^2 2*(mR*vB+vA)x -((mR-1)*vB^2+2*vA*vB)=0
        //first we find the discriminant
        double a = -(mR + 1);
        double b = 2 * (mR * vB + vA);
        double c = -((mR - 1) * vB * vB + 2 * vA * vB);
        double discriminant = Math.sqrt(b * b - 4 * a * c);
        double root = (-b + discriminant)/(2 * a);

        //only one of the roots is the solution, the other pertains to the current velocities
        if (root - vB < 0.01) {
            root = (-b - discriminant)/(2 * a);
        }

        //The resulting changes in velocity for ball A and B
        Point2D deltaVA = collisionVector.multiply(mR * (vB - root));
        Point2D deltaVB = collisionVector.multiply(root - vB);

        itemA.setxVel(itemA.getxVel() + deltaVA.getX());
        itemA.setyVel(itemA.getyVel() + deltaVA.getY());
        itemB.setxVel(itemB.getxVel() + deltaVB.getX());
        itemB.setyVel(itemB.getyVel() + deltaVB.getY());
    }
}
