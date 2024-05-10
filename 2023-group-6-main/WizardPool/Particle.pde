
class Particle {
  PVector pos, vel, acc;
  float r;
  int col;
  float lifespan = 255;

  Particle(float x, float y, int col) {
    pos = new PVector(x, y);
    r = random(2, 8);
    vel = PVector.random2D();
    vel.mult(random(1, 10));
    acc = new PVector(0, random(0.01, 0.05));
    this.col = col;
  }

  void update() {
    vel.add(acc);
    pos.add(vel);
    lifespan -= 4;
  }

  boolean done() {
    if (lifespan < 0) {
      return true;
    } else {
      return false;
    }
  }

  void show() {
    noStroke();
    fill(red(col), green(col), blue(col), lifespan);
    ellipse(pos.x, pos.y, r, r);
  }
}
