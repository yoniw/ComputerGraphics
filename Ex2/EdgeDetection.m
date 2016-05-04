M = imread('lena.bmp');
[m,n] = size(M);
DX(1:m, 1:n) = 0;
DY(1:m, 1:n) = 0;
G(1:m, 1:n) = 0;

% Computing DX
% index goes through 2:m-1 so we don't access illegal indexes 
for i = 2:m-1
    for j = 2:n-1
        DX(i,j) = 2*M(i-1,j-1)-2*M(i-1,j+1)+M(i,j-1)-M(i,j+1)+2*M(i+1,j-1)-2*M(i+1,j+1);
    end
end;

% Computing DY
for i = 2:m-1
    for j = 2:n-1
        DY(i,j) = 2*M(i-1,j-1)+M(i-1,j)+M(i-1,j+1)-2*M(i+1,j-1)-M(i+1,j)-2*M(i+1,j+1);
    end
end

% Computing gradient
for i = 1:m
    for j = 1:n
        G(i,j) = sqrt(DX(i,j)^2+DY(i,j)^2);
    end
end

% normalize G
mmin = min(G(:));
mmax = max(G(:));
G = (G-mmin)./(mmax-mmin);

imshow(G);