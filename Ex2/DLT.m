function [ H ] = DLT(matches)

n = size(matches,1);

homMatches = getHomMatrix(matches, n);
%howMatches is a 6xn matrix

[T_first, T_second] = getTs(homMatches);
A = assembleA(T_first, T_second,homMatches, n);
h = geth_col(A);
H = getH(h);
% denormalize H
H = inv(T_second)*H*T_first;

% so H' would be in a projective transformation form
H = H/H(3,3);
%H(1,3) = 0;
%H(2,3) = 0;

end


function [ H ] = getH(h)
H = zeros(3,3);

H(:,1) = h(1:3);
H(:,2) = h(4:6);
H(:,3) = h(7:9);

end


function [ h ] = geth_col(matrix)
[U,S,V] = svd(matrix);
numVCols = size(V,2);
h = V(:,numVCols);

end

% creates Ai's 'on live' and puts them into A
function [ result ] = assembleA(T_first, T_second, hommMatches, n)
result = zeros(2*n, 9);
zeros_vector = [0 0 0];

for i =1:n
   vector_xi = T_first*hommMatches(1:3,i);
   vector_xi_tag = T_second*hommMatches(4:6,i);
   
   xi_tag = vector_xi_tag(1);
   yi_tag = vector_xi_tag(2);
   wi_tag = vector_xi_tag(3);
   
   result(2*i-1, :) = [zeros_vector -wi_tag*vector_xi' yi_tag*vector_xi'];
   result(2*i, :) = [wi_tag*vector_xi' zeros_vector -xi_tag*vector_xi'];
   
end

end

function [ T_first, T_second ] = getTs(homMatches)

Ttranslate_first = getTranslated(mean(homMatches(1:3, :),2));
Tscale_first = getScaled(Ttranslate_first*homMatches(1:3, :));
T_first = Tscale_first*Ttranslate_first;


Ttranslate_second = getTranslated(mean(homMatches(4:6, :),2));
Tscale_second = getScaled(Ttranslate_second*homMatches(4:6, :));
T_second = Tscale_second*Ttranslate_second;


end

function [ result ] = getScaled(matrix)

result = zeros(3,3); 
result(3,3) = 1;

sumNorms = 0;
for i=1:size(matrix,2)
    sumNorms = sumNorms + norm(matrix(1:2, i));
end

result(1,1) = (size(matrix,2)*sqrt(2))/sumNorms;
result(2,2) = (size(matrix,2)*sqrt(2))/sumNorms;

end

function [ result ] = getTranslated(meansVector)

result = eye(3,3);
result(1,3) = -meansVector(1);
result(2,3) = -meansVector(2);

end

function [ result ] = getHomMatrix(matches, n)

result = ones(6,n);

for i=1:n
    result(1:2,i) = matches(i,1:2);
    result(4:5,i) = matches(i,3:4);
end

end