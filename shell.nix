let
  nixpkgs = import <nixpkgs> {};
in
  with nixpkgs;

  stdenv.mkDerivation {
    name = "school-ICSS";
    buildInputs = [
      jdk
      maven
      antlr

      javaPackages.openjfx17
    ];
  }
