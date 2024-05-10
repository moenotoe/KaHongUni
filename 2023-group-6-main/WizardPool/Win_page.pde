class Firework {
  PVector pos, vel, acc;
  float r;
  int b, col;
  boolean exploded = false;
  ArrayList<Particle> particles;

  Firework() {
    r = random(4, 10);
    pos = new PVector(random(width), height);
    vel = new PVector(0, random(-12, -8));
    acc = new PVector(0, random(0.2, 0.5));
    col = color(random(255), random(255), random(255));
    b = int(random(50, 200));
    particles = new ArrayList<Particle>();
  }

  void update() {
    if (!exploded) {
      vel.add(acc);
      pos.add(vel);
      if (vel.y >= 0) {
        exploded = true;
        explode();
      }
    }
    for (int i = particles.size() - 1; i >= 0; i--) {
      particles.get(i).update();
      if (particles.get(i).done()) {
        particles.remove(i);
      }
    }
  }

  void show() {
    if (!exploded) {
      stroke(col, b);
      strokeWeight(r);
      point(pos.x, pos.y);
    }
    for (Particle p : particles) {
      p.show();
    }
  }

  void explode() {
    for (int i = 0; i < 100; i++) {
      Particle p = new Particle(pos.x, pos.y, col);
      particles.add(p);
    }
  }
}
